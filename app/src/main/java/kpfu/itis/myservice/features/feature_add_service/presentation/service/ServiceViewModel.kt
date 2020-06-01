package kpfu.itis.myservice.features.feature_add_service.presentation.service

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.features.feature_add_service.domain.ServiceInteractor
import javax.inject.Inject

class ServiceViewModel  @Inject constructor(
    private val interactor: ServiceInteractor
) : ViewModel()  {

    private var disposable: Disposable? = null

    private lateinit var serviceLiveData: MutableLiveData<Result<Service>>
    private lateinit var isDeleteLiveData: MutableLiveData<Result<Boolean>>
    private lateinit var loading: MutableLiveData<Boolean>

    @MainThread
    fun getService(id: Long): LiveData<Result<Service>> {
        serviceLiveData = MutableLiveData()
        disposable = interactor
            .getService(id)
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
    fun deleteService(id: Long): LiveData<Result<Boolean>> {
        isDeleteLiveData = MutableLiveData()
        disposable = interactor
            .deleteService(id)
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

    fun isContained(userId: Long) : Boolean = interactor.isContained(userId)

    override fun onCleared() {
        disposable?.dispose()
    }

}
