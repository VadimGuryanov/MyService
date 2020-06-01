package kpfu.itis.myservice.features.feature_profile.presentation.services

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.features.feature_profile.domain.ProfileInteractor
import kpfu.itis.myservice.features.feature_profile.presentation.services.di.ProfileServicesScope
import javax.inject.Inject

@ProfileServicesScope
class ProfileServicesViewModel @Inject constructor(
    private var interactor: ProfileInteractor
) : ViewModel() {

    private var disposable: Disposable? = null

    private var servicesLiveData: MutableLiveData<Result<List<Service>>>? = null
    private lateinit var favoritesLiveData: MutableLiveData<Result<List<Favorite>>>
    private lateinit var isDeleteLiveData: MutableLiveData<Result<Boolean>>
    private lateinit var isAddLiveData: MutableLiveData<Result<Boolean>>
    private lateinit var loading: MutableLiveData<Boolean>

    fun services() : LiveData<Result<List<Service>>> {
        if (servicesLiveData == null) {
            servicesLiveData = MutableLiveData()
        }
        return servicesLiveData ?: MutableLiveData()
    }

    @MainThread
    fun getServices(id: Long) {
        disposable = interactor
            .getServices(id)
            .doOnSubscribe{loading.postValue(true)}
            .doAfterTerminate{loading.postValue(false)}
            .subscribeBy(
                onSuccess = {
                    val sup = servicesLiveData
                    sup?.postValue(Result.success(it))
                    servicesLiveData = sup
                },
                onError = {
                    val sup = servicesLiveData
                    sup?.postValue(Result.failure(it))
                    servicesLiveData = sup
                }
            )
    }

    @MainThread
    fun deleteFavorite(id: Long, userId: Long) : LiveData<Result<Boolean>> {
        isDeleteLiveData = MutableLiveData()
        disposable = interactor
            .deleteFavorite(id, userId)
            .subscribeBy(
                onComplete = {
                    val sup = isDeleteLiveData
                    sup.postValue(Result.success(true))
                    isDeleteLiveData = sup
                },
                onError = {
                    val sup = isDeleteLiveData
                    sup.postValue(Result.failure(it))
                    isDeleteLiveData = sup
                }
            )
        return isDeleteLiveData
    }


    @MainThread
    fun addFavorite(id: Long, userId: Long) : LiveData<Result<Boolean>> {
        isAddLiveData = MutableLiveData()
        disposable = interactor
            .addFavorite(id, userId)
            .subscribeBy(
                onComplete = {
                    val sup = isAddLiveData
                    sup.postValue(Result.success(true))
                    isAddLiveData = sup
                },
                onError = {
                    val sup = isAddLiveData
                    sup.postValue(Result.failure(it))
                    isAddLiveData = sup
                }
            )
        return isAddLiveData
    }

    @MainThread
    fun getFavorites() : LiveData<Result<List<Favorite>>> {
        favoritesLiveData = MutableLiveData()
        disposable = interactor
            .getFavorites()
            .subscribeBy(
                onSuccess = {
                    val sup = favoritesLiveData
                    sup.postValue(Result.success(it))
                    favoritesLiveData = sup
                },
                onError = {
                    val sup = favoritesLiveData
                    sup.postValue(Result.failure(it))
                    favoritesLiveData = sup
                }
            )
        return favoritesLiveData
    }

    @MainThread
    fun isLoading() : LiveData<Boolean> {
        loading = MutableLiveData()
        return loading
    }

    fun isAuth() : Boolean = interactor.isAuth()

    override fun onCleared() {
        disposable?.dispose()
    }

    fun getId(): Long = interactor.getID()

}
