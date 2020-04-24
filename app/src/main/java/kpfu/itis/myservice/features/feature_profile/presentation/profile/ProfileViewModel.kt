package kpfu.itis.myservice.features.feature_profile.presentation.profile

import android.widget.ImageView
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.data.db.models.UserLocal
import kpfu.itis.myservice.common.Response
import kpfu.itis.myservice.features.feature_profile.domain.ProfileInteractor
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val interactor: ProfileInteractor,
    private val helper: HelperSharedPreferences
) : ViewModel() {

    private var disposable: Disposable? = null
    private lateinit var profileLiveData: MutableLiveData<Response<UserLocal>>
    private lateinit var loadingLiveData: MutableLiveData<Boolean>

    @MainThread
    fun getUserProfile(): LiveData<Response<UserLocal>> {
        profileLiveData = MutableLiveData()
        disposable = interactor
            .getUser(helper.readID()?.toLong() ?: -1)
            .cache()
            .doOnSubscribe{loadingLiveData.setValue(true)}
            .doAfterNext{loadingLiveData.setValue(false)}
            .subscribeBy(
                onNext = {
                    val countriesLiveDataImm = profileLiveData
                    countriesLiveDataImm.value = Response.success(it)
                    profileLiveData = countriesLiveDataImm
                },
                onError = {
                    val countriesLiveDataImm = profileLiveData
                    countriesLiveDataImm.value = Response.error(it)
                    profileLiveData = countriesLiveDataImm
                }
            )
        return profileLiveData
    }

    @MainThread
    fun isLoading() : LiveData<Boolean> {
        loadingLiveData = MutableLiveData()
        return loadingLiveData
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

}
