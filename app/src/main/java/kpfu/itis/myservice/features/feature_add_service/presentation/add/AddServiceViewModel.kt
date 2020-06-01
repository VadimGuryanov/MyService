package kpfu.itis.myservice.features.feature_add_service.presentation.add

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User
import kpfu.itis.myservice.features.feature_add_service.domain.ServiceInteractor
import kpfu.itis.myservice.features.feature_add_service.presentation.add.di.AddServiceScope
import javax.inject.Inject

@AddServiceScope
class AddServiceViewModel  @Inject constructor(
    private val interactor: ServiceInteractor
) : ViewModel()  {

    private var disposable: Disposable? = null

    private lateinit var isAdd : MutableLiveData<Result<Boolean>>
    private lateinit var isEdit : MutableLiveData<Result<Boolean>>
    private lateinit var loading: MutableLiveData<Boolean>
    private lateinit var service: MutableLiveData<Result<Service>>
    private lateinit var user: MutableLiveData<Result<User>>

    @MainThread
    fun addService(service: Service): LiveData<Result<Boolean>> {
        isAdd = MutableLiveData()
        disposable = interactor
            .addService(service)
            .subscribeBy(
                onComplete = {
                    val sup = isAdd
                    sup.postValue(Result.success(true))
                    isAdd = sup
                },
                onError = {
                    val sup = isAdd
                    sup.postValue(Result.failure(it))
                    isAdd = sup
                }
            )
        return isAdd
    }

    @MainThread
    fun updateService(service : Service) : LiveData<Result<Boolean>> {
        isEdit = MutableLiveData()
        disposable = interactor
            .updateService(service)
            .subscribeBy(
                onComplete = {
                    val sup = isEdit
                    sup.postValue(Result.success(true))
                    isEdit = sup
                },
                onError = {
                    val sup = isEdit
                    sup.postValue(Result.failure(it))
                    isEdit = sup
                }
            )
        return isEdit
    }

    @MainThread
    fun getService(id: Long) : LiveData<Result<Service>> {
        service = MutableLiveData()
        disposable = interactor
            .getService(id)
            .doOnSubscribe{loading.postValue(true)}
            .doAfterTerminate{loading.postValue(false)}
            .subscribeBy(
                onSuccess = {
                    val sup = service
                    sup.postValue(Result.success(it))
                    service = sup
                },
                onError = {
                    val sup = service
                    sup.postValue(Result.failure(it))
                    service = sup
                }
            )
        return service
    }

    @MainThread
    fun getUser() : LiveData<Result<User>> {
        user = MutableLiveData()
        disposable = interactor
            .getUser()
            .doOnSubscribe{loading.postValue(true)}
            .doAfterTerminate{loading.postValue(false)}
            .subscribeBy(
                onSuccess = {
                    val sup = user
                    sup.postValue(Result.success(it))
                    user = sup
                },
                onError = {
                    val sup = user
                    sup.postValue(Result.failure(it))
                    user = sup
                }
            )
        return user
    }

    @MainThread
    fun isLoading() : LiveData<Boolean> {
        loading = MutableLiveData()
        return loading
    }

    override fun onCleared() {
        disposable?.dispose()
    }

}
