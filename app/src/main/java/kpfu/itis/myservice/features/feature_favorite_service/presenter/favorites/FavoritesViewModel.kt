package kpfu.itis.myservice.features.feature_favorite_service.presenter.favorites

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.features.feature_favorite_service.domain.FavoritesInteractor
import kpfu.itis.myservice.features.feature_favorite_service.presenter.di.FavoritesScope
import javax.inject.Inject

@FavoritesScope
class FavoritesViewModel @Inject constructor(
    private var interactor: FavoritesInteractor
) : ViewModel() {

    private var disposable: Disposable? = null

    private lateinit var favoritesLiveData: MutableLiveData<Result<List<Favorite>>>
    private lateinit var isDeleteLiveData: MutableLiveData<Result<Boolean>>
    private lateinit var loading: MutableLiveData<Boolean>

    @MainThread
    fun getFavorites() : LiveData<Result<List<Favorite>>> {
        favoritesLiveData = MutableLiveData()
        disposable = interactor
            .getFavorites()
            .doOnSubscribe{loading.postValue(true)}
            .doAfterTerminate{loading.postValue(false)}
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
    fun deleteFavorite(id: Long) : LiveData<Result<Boolean>> {
        isDeleteLiveData = MutableLiveData()
        disposable = interactor
            .deleteFavorite(id)
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
    fun isLoading() : LiveData<Boolean> {
        loading = MutableLiveData()
        return loading
    }

    fun isAuth() : Boolean = interactor.isAuth()

    override fun onCleared() {
        disposable?.dispose()
    }

}
