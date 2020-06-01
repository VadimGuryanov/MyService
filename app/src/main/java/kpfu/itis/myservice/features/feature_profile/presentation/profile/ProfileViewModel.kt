package kpfu.itis.myservice.features.feature_profile.presentation.profile

import android.widget.ImageView
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.data.db.models.User
import kpfu.itis.myservice.features.feature_profile.domain.ProfileInteractor
import kpfu.itis.myservice.features.feature_profile.presentation.edit.dto.UserDto
import kpfu.itis.myservice.features.feature_profile.presentation.profile.di.ProfileScope
import javax.inject.Inject

@ProfileScope
class ProfileViewModel @Inject constructor(
    private val interactor: ProfileInteractor
) : ViewModel() {

    private var disposable: Disposable? = null
    val profileLiveData: MutableLiveData<Result<User>> by lazy {
        MutableLiveData<Result<User>>()
    }
    val loadingLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    private lateinit var descriptionLiveData: MutableLiveData<Result<Boolean>>
    private lateinit var exitLiveData: MutableLiveData<Result<Boolean>>

    fun getUserProfile(id: Long) {
        disposable = interactor
            .getUser(id)
            .doOnSubscribe{loadingLiveData.postValue(true)}
            .doAfterTerminate{loadingLiveData.postValue(false)}
            .subscribeBy(
                onSuccess = {
                    profileLiveData.value = Result.success(it)
                },
                onError = {
                    profileLiveData.value = Result.failure(it)
                }
            )
    }

    @MainThread
    fun deleteDescription(): LiveData<Result<Boolean>> {
        descriptionLiveData = MutableLiveData()
        disposable = interactor
            .deleteDescription()
            .subscribeBy(
                onComplete = {
                    val sup = descriptionLiveData
                    sup.postValue(Result.success(true))
                    descriptionLiveData = sup
                },
                onError = {
                    val sup = descriptionLiveData
                    sup.postValue(Result.failure(it))
                    descriptionLiveData = sup
                }
            )
        return descriptionLiveData
    }

    @MainThread
    fun exit() : LiveData<Result<Boolean>> {
        exitLiveData = MutableLiveData()
        disposable = interactor
            .exit()
            .subscribeBy(
                onComplete = {
                    val sup = exitLiveData
                    sup.postValue(Result.success(true))
                    exitLiveData = sup
                },
                onError = {
                    val sup = exitLiveData
                    sup.postValue(Result.failure(it))
                    exitLiveData = sup
                }
            )
        return exitLiveData
    }

    fun download(view: ImageView, url: String) {
        Glide
            .with(view.context)
            .load(url)
            .centerCrop()
            .into(view)
    }

    override fun onCleared() {
        disposable?.dispose()
    }

    fun getId(): Long = interactor.getID()

}
