package kpfu.itis.myservice.features.feature_search.domain

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.data.db.models.Notification
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User
import kpfu.itis.myservice.features.feature_search.data.repository.SearchRepository
import kpfu.itis.myservice.features.feature_search.domain.di.SearchDomainScope
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@SearchDomainScope
class SearchInteractorImpl @Inject constructor(
    private var repository: SearchRepository,
    private var helper: HelperSharedPreferences
) : SearchInteractor {

    private val dateFormat: DateFormat by lazy {
        SimpleDateFormat.getDateInstance()
    }

    override fun getServices(): Single<List<Service>> =
        repository.getServices()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())

    override fun getServices(query: String) : Single<List<Service>> =
        repository.getServices(query)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())

    override fun addFavorite(id: Long, userId: Long): Completable =
        repository.addFavorite(id, userId)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())

    override fun deleteFavorite(id: Long, userId: Long): Completable =
        repository.deleteFavorite(id, userId)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())

    override fun isAuth(): Boolean =
        helper.readSession()?.let {
            helper.readID().let {
                it?.toLong() ?: -1 >= 0
            }
        } ?: false

    override fun getFavorites(): Single<List<Favorite>> =
        if (isAuth()) {
            repository.getFavorites()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
        } else {
            Single.just(listOf())
        }

    override fun getService(id: Long, userId: Long): Single<Service> =
        repository.getService(id, userId)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())

    override fun isEqualsAuthor(id: Long): Boolean =
        if (isAuth()) {
            id == helper.readID()?.toLong() ?: -1
        } else {
            false
        }

    override fun sendMessage(message: Notification): Completable =
        if (isAuth()) {
            message.from_user_id = helper.readID()?.toLong() ?: -1
            repository.getCount()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMapCompletable {
                    message.mess_id = it + 1
                    message.data = getTime()
                    repository.sendMessage(message)
                }
        } else {
            Completable.error(Exception("Вы не авторизовались"))
        }

    override fun getUser(id: Long): Single<User> =
        if (isAuth()) {
            repository.getUser(id)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
        } else {
            Single.error(Exception())
        }

    override fun getUser(): Single<User> =
        if (isAuth()) {
            repository.getUser(helper.readID()?.toLong() ?: -1)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
        } else {
            Single.error(Exception())
        }

    override fun getId(): Long = helper.readID()?.toLong() ?: -1

    private fun getTime() : String = dateFormat.format(Date())

}
