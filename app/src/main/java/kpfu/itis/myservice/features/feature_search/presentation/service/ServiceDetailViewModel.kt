package kpfu.itis.myservice.features.feature_search.presentation.service

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.features.feature_search.domain.SearchInteractor
import kpfu.itis.myservice.features.feature_search.presentation.service.di.ServiceDetailScope
import javax.inject.Inject

@ServiceDetailScope
class ServiceDetailViewModel  @Inject constructor(
    private val interactor: SearchInteractor
) : ViewModel() {

    private var disposable: Disposable? = null

    private lateinit var serviceLiveData: MutableLiveData<Result<Service>>
    private lateinit var deleteFavoriteLiveData: MutableLiveData<Result<Boolean>>
    private lateinit var addFavoriteLiveData: MutableLiveData<Result<Boolean>>
    private lateinit var loading: MutableLiveData<Boolean>

    @MainThread
    fun getService(id: Long, userId: Long) : LiveData<Result<Service>> {
        serviceLiveData = MutableLiveData()
        disposable = interactor
            .getService(id, userId)
            .doOnSubscribe{loading.postValue(true)}
            .doAfterTerminate{loading.postValue(false)}
            .subscribeBy(
                onSuccess = {
                    val sup = serviceLiveData
                    sup.postValue(Result.success(it))
                    serviceLiveData = sup
                },
                onError = {
                    val sup = serviceLiveData
                    sup.postValue(Result.failure(it))
                    serviceLiveData = sup
                }
            )
        return serviceLiveData
    }

    @MainThread
    fun deleteFavorite(id: Long, userId: Long) : LiveData<Result<Boolean>> {
        deleteFavoriteLiveData = MutableLiveData()
        disposable = interactor
            .deleteFavorite(id, userId)
            .subscribeBy(
                onComplete = {
                    val sup = deleteFavoriteLiveData
                    sup.postValue(Result.success(true))
                    deleteFavoriteLiveData = sup
                },
                onError = {
                    val sup = deleteFavoriteLiveData
                    sup.postValue(Result.failure(it))
                    deleteFavoriteLiveData = sup
                }
            )
        return deleteFavoriteLiveData
    }


    @MainThread
    fun addFavorite(id: Long, userId: Long) : LiveData<Result<Boolean>> {
        addFavoriteLiveData = MutableLiveData()
        disposable = interactor
            .addFavorite(id, userId)
            .subscribeBy(
                onComplete = {
                    val sup = addFavoriteLiveData
                    sup.postValue(Result.success(true))
                    addFavoriteLiveData = sup
                },
                onError = {
                    val sup = addFavoriteLiveData
                    sup.postValue(Result.failure(it))
                    addFavoriteLiveData = sup
                }
            )
        return addFavoriteLiveData
    }

    @MainThread
    fun isLoading() : LiveData<Boolean> {
        loading = MutableLiveData()
        return loading
    }

    fun isAuth() : Boolean = interactor.isAuth()

    fun isEqualsAuthor(id: Long) : Boolean = interactor.isEqualsAuthor(id)

    override fun onCleared() {
        disposable?.dispose()
    }

}
