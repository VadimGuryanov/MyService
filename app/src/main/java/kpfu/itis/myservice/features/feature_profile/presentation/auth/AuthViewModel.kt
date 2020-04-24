package kpfu.itis.myservice.features.feature_profile.presentation.auth

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kpfu.itis.myservice.common.Response
import kpfu.itis.myservice.features.feature_profile.domain.ProfileInteractor
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val interactor: ProfileInteractor
) : ViewModel() {

    private var disposable: Disposable? = null
    private lateinit var isAuthLiveData: MutableLiveData<Response<Boolean>>

    @MainThread
    fun authUserProfile(): LiveData<Response<Boolean>> {
        isAuthLiveData = MutableLiveData()
        disposable = interactor
            .auth()
            .subscribeBy(
                onSuccess = {
                    val sup = isAuthLiveData
                    sup.value = Response.success(it)
                    isAuthLiveData = sup
                },
                onError = {
                    val sup = isAuthLiveData
                    sup.value = Response.error(it)
                    isAuthLiveData = sup
                }
            )
        return isAuthLiveData
    }

    override fun onCleared() {
        disposable?.dispose()
    }

}
