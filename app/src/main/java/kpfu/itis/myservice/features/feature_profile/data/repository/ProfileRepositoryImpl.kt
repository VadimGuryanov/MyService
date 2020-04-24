package kpfu.itis.myservice.features.feature_profile.data.repository

import android.util.Log
import com.vk.api.sdk.VK
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.common.exceptions.DatabaseException
import kpfu.itis.myservice.data.db.dao.UserDao
import kpfu.itis.myservice.data.db.models.UserLocal
import kpfu.itis.myservice.features.feature_profile.data.model.VKUser
import kpfu.itis.myservice.features.feature_profile.data.network.VKUsersRequest


class ProfileRepositoryImpl(
    private val dao: UserDao,
    private val helper: HelperSharedPreferences
) : ProfileRepository {

    private var userLocal: UserLocal? = null

    override fun getUser(id: Long): Flowable<UserLocal> =
        dao.getUserLocalById(id)

    override fun authUser() {
        Observable.fromCallable {
            VK.executeSync(VKUsersRequest(intArrayOf(helper.readID()?.toInt() ?: -1)))
        }
            .subscribeOn(Schedulers.single())
            .observeOn(Schedulers.io())
            .subscribeBy(
                onNext = { result ->
                    Log.e("vkuser", result[0].toString())
                    result[0].apply {
                        getUserLocalById(id)
                        userLocal?.let {
                            dao.updateUserLocal(
                                updateWithVKUser(it, this)
                            )
                        } ?: saveVKUser(result[0])
                    }
                },
                onError = {
                    Log.e("vk", it.message ?: "VK request error")
                    throw DatabaseException(it.message ?: "VK request error")
                }
            )
    }

    override fun addDescription(description: String?) {
        helper.readID()?.toLong()?.let {
            getUser(it)
                .subscribeBy(
                    onNext = {
                        it.description = description
                        dao.updateUserLocal(it)
                    },
                    onError = {
                        Log.e("add description", it.message ?: "add description error")
                        throw DatabaseException(it.message ?: "add description error")
                    }
                )
        }
    }

    private fun saveVKUser(user: VKUser) =
        user.apply {
            dao.insertUserLocal(
                UserLocal(
                    id,
                    first_name,
                    last_name,
                    city?.title,
                    photo_200_orig,
                    mobile_phone,
                    universities?.get(0)?.name,
                    universities?.get(0)?.faculty_name,
                    null,
                    null
                )
            )
        }

    private fun updateWithVKUser(userLocal: UserLocal, vkUser: VKUser) : UserLocal =
        userLocal.also {
            vkUser.apply {
                it.vk_id = id
                it.city = city?.title
                it.name = first_name
                it.lastName = last_name
                it.faculty = universities?.get(0)?.faculty_name
                it.university = universities?.get(0)?.name
                it.mobilePhone = mobile_phone
                it.photoURL = photo_200_orig
            }
        }

    private fun getUserLocalById(id: Long) : Disposable =
        dao.getUserLocalById(id).subscribeBy(
            onNext = {
                userLocal = it
            },
            onError = {}
        )

}
