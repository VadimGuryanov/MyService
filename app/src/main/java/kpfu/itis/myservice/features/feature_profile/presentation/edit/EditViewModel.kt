package kpfu.itis.myservice.features.feature_profile.presentation.edit

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kpfu.itis.myservice.data.db.models.User
import kpfu.itis.myservice.features.feature_profile.domain.ProfileInteractor
import kpfu.itis.myservice.features.feature_profile.presentation.edit.di.EditScope
import kpfu.itis.myservice.features.feature_profile.presentation.edit.dto.UserDto
import javax.inject.Inject

@EditScope
class EditViewModel  @Inject constructor(
    private val interactor: ProfileInteractor
) : ViewModel() {

    private var disposable: Disposable? = null
    private lateinit var editLiveData: MutableLiveData<Result<Boolean>>
    private lateinit var profileLiveData: MutableLiveData<Result<User>>
    private lateinit var loadingLiveData: MutableLiveData<Boolean>

    @MainThread
    fun updateUserProfile(userDto: UserDto): LiveData<Result<Boolean>> {
        editLiveData = MutableLiveData()
        disposable = interactor
            .updateUser(userDto)
            .cache()
            .subscribeBy(
                onComplete = {
                    val countriesLiveDataImm = editLiveData
                    countriesLiveDataImm.postValue(Result.success(true))
                    editLiveData = countriesLiveDataImm
                },
                onError = {
                    val countriesLiveDataImm = editLiveData
                    countriesLiveDataImm.postValue(Result.failure(it))
                    editLiveData = countriesLiveDataImm
                }
            )
        return editLiveData
    }

    @MainThread
    fun getUserProfile(id: Long): LiveData<Result<User>> {
        profileLiveData = MutableLiveData()
        disposable = interactor
            .getUser(id)
            .cache()
            .doOnSubscribe{loadingLiveData.postValue(true)}
            .doAfterTerminate{loadingLiveData.postValue(false)}
            .subscribeBy(
                onSuccess = {
                    val supLiveDataImm = profileLiveData
                    supLiveDataImm.postValue(Result.success(it))
                    profileLiveData = supLiveDataImm
                },
                onError = {
                    val supLiveDataImm = profileLiveData
                    supLiveDataImm.postValue(Result.failure(it))
                    profileLiveData = supLiveDataImm
                }
            )
        return profileLiveData
    }

    @MainThread
    fun isLoading() : LiveData<Boolean> {
        loadingLiveData = MutableLiveData()
        return loadingLiveData
    }

    override fun onCleared() {
        disposable?.dispose()
    }

}
