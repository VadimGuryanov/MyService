package kpfu.itis.myservice.features.feature_profile.presentation.about_me

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kpfu.itis.myservice.features.feature_profile.domain.ProfileInteractor
import kpfu.itis.myservice.features.feature_profile.presentation.about_me.di.AboutMeScope
import kpfu.itis.myservice.features.feature_profile.presentation.profile.di.ProfileScope
import javax.inject.Inject

@AboutMeScope
class AboutMeViewModel @Inject constructor(
    private val interactor: ProfileInteractor
) : ViewModel() {

    private var disposable: Disposable? = null
    private lateinit var aboutMeLiveData: MutableLiveData<Result<Boolean>>

    @MainThread
    fun addDescription(description: String): LiveData<Result<Boolean>> {
        aboutMeLiveData = MutableLiveData()
        disposable = interactor
            .addDescription(description)
            .subscribeBy(
                onComplete = {
                    val sup = aboutMeLiveData
                    sup.postValue(Result.success(true))
                    aboutMeLiveData = sup
                },
                onError = {
                    val sup = aboutMeLiveData
                    sup.postValue(Result.failure(it))
                    aboutMeLiveData = sup
                }
            )
        return aboutMeLiveData
    }

    override fun onCleared() {
        disposable?.dispose()
    }

}
