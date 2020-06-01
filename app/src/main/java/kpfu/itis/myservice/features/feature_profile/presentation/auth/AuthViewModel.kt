package kpfu.itis.myservice.features.feature_profile.presentation.auth

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kpfu.itis.myservice.features.feature_profile.domain.ProfileInteractor
import kpfu.itis.myservice.features.feature_profile.presentation.auth.di.AuthScope
import kpfu.itis.myservice.features.feature_profile.presentation.profile.di.ProfileScope
import javax.inject.Inject

@AuthScope
class AuthViewModel @Inject constructor(
    private val interactor: ProfileInteractor
) : ViewModel() {

    private var disposable: Disposable? = null
    private lateinit var authLiveData: MutableLiveData<Result<Boolean>>

    @MainThread
    fun authUserProfile(): LiveData<Result<Boolean>> {
        authLiveData = MutableLiveData()
        disposable = interactor
            .auth()
            .subscribeBy(
                onComplete = {
                    val sup = authLiveData
                    sup.postValue(Result.success(true))
                    authLiveData = sup
                },
                onError = {
                    val sup = authLiveData
                    sup.postValue(Result.failure(it))
                    authLiveData = sup
                }
            )
        return authLiveData
    }

    @MainThread
    fun isAuth() : Boolean =
        interactor.isAuth()

    @MainThread
    fun getID() : Long =
        interactor.getID()

    override fun onCleared() {
        disposable?.dispose()
    }

}
