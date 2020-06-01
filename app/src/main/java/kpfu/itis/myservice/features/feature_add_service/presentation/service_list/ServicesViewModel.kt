package kpfu.itis.myservice.features.feature_add_service.presentation.service_list

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.features.feature_add_service.domain.ServiceInteractor
import javax.inject.Inject

class ServicesViewModel @Inject constructor(
    private val interactor: ServiceInteractor
) : ViewModel()  {

    private var disposable: Disposable? = null

    private lateinit var servicesLiveData: MutableLiveData<Result<List<Service>>>
    private lateinit var loading: MutableLiveData<Boolean>

    @MainThread
    fun getServices(): LiveData<Result<List<Service>>> {
        servicesLiveData = MutableLiveData()
        disposable = interactor
            .getServices()
            .doOnSubscribe{loading.postValue(true)}
            .doAfterTerminate{loading.postValue(false)}
            .subscribeBy(
                onSuccess = {
                    val sup = servicesLiveData
                    sup.postValue(Result.success(it))
                    servicesLiveData = sup
                },
                onError = {
                    val sup = servicesLiveData
                    sup.postValue(Result.failure(it))
                    servicesLiveData = sup
                }
            )
        return servicesLiveData
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
