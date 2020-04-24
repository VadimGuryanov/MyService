package kpfu.itis.myservice.features.feature_profile.domain

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.common.exceptions.DatabaseException
import kpfu.itis.myservice.data.db.models.UserLocal
import kpfu.itis.myservice.features.feature_profile.data.repository.ProfileRepository

class ProfileInteractorImpl (
    private val repository: ProfileRepository,
    private val helper: HelperSharedPreferences
) : ProfileInteractor{

    override fun auth() : Single<Boolean> {
        var isOK = try {
            helper.apply {
                if (readSession() == null) {
                    repository.authUser()
                    editSession("auth")
                }
            }
            true
        } catch (ex: DatabaseException) {
            false
        }
        return Single.just(isOK)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }



    override fun getUser(id :Long): Flowable<UserLocal> =
        repository.getUser(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun addDescription(description: String?) : Single<Boolean> {
        var isOk = try {
            repository.addDescription(description)
            true
        } catch (ex: DatabaseException) {
            false
        }
        return Single.just(isOk)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}
