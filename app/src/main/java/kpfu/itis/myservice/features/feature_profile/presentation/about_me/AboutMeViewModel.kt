package kpfu.itis.myservice.features.feature_profile.presentation.about_me

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kpfu.itis.myservice.common.Response
import kpfu.itis.myservice.features.feature_profile.domain.ProfileInteractor
import javax.inject.Inject

class AboutMeViewModel @Inject constructor(
    private val interactor: ProfileInteractor
) : ViewModel() {

    private var disposable: Disposable? = null
    private lateinit var profileLiveData: MutableLiveData<Response<Boolean>>

    @MainThread
    fun addDescription(description: String?): LiveData<Response<Boolean>> {
        profileLiveData = MutableLiveData()
        disposable = interactor
            .addDescription(description)
            .subscribeBy(
                onSuccess = {
                    val sup = profileLiveData
                    sup.value = Response.success(it)
                    profileLiveData = sup
                },
                onError = {
                    val sup = profileLiveData
                    sup.value = Response.error(it)
                    profileLiveData = sup
                }
            )
        return profileLiveData
    }

    override fun onCleared() {
        disposable?.dispose()
    }

}
