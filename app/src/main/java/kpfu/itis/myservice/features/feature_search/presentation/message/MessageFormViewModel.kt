package kpfu.itis.myservice.features.feature_search.presentation.message

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kpfu.itis.myservice.data.db.models.Notification
import kpfu.itis.myservice.data.db.models.User
import kpfu.itis.myservice.features.feature_search.domain.SearchInteractor
import kpfu.itis.myservice.features.feature_search.presentation.message.di.MessageFormScope
import javax.inject.Inject

@MessageFormScope
class MessageFormViewModel @Inject constructor(
    private var interactor: SearchInteractor
) : ViewModel() {

    private var disposable: Disposable? = null

    private lateinit var sendLiveData: MutableLiveData<Result<Boolean>>
    private lateinit var userLiveData: MutableLiveData<Result<User>>
    private lateinit var currantUserLiveData: MutableLiveData<Result<User>>

    @MainThread
    fun sendMessage(message: Notification) : LiveData<Result<Boolean>> {
        sendLiveData = MutableLiveData()
        disposable = interactor
            .sendMessage(message)
            .subscribeBy(
                onComplete = {
                    val sup = sendLiveData
                    sup.postValue(Result.success(true))
                    sendLiveData = sup
                },
                onError = {
                    val sup = sendLiveData
                    sup.postValue(Result.failure(it))
                    Log.e("sendMessageERR", it.toString())
                    sendLiveData = sup
                }
            )
        return sendLiveData
    }

    @MainThread
    fun getUser(id: Long) : LiveData<Result<User>> {
        userLiveData = MutableLiveData()
        disposable = interactor
            .getUser(id)
            .subscribeBy(
                onSuccess = {
                    val sup = userLiveData
                    sup.postValue(Result.success(it))
                    userLiveData = sup
                },
                onError = {
                    val sup = userLiveData
                    sup.postValue(Result.failure(it))
                    userLiveData = sup
                }
            )
        return userLiveData
    }

    @MainThread
    fun getUser() : LiveData<Result<User>> {
        currantUserLiveData = MutableLiveData()
        disposable = interactor
            .getUser()
            .subscribeBy(
                onSuccess = {
                    val sup = currantUserLiveData
                    sup.postValue(Result.success(it))
                    currantUserLiveData = sup
                },
                onError = {
                    val sup = currantUserLiveData
                    sup.postValue(Result.failure(it))
                    currantUserLiveData = sup
                }
            )
        return currantUserLiveData
    }

    fun isAuth() : Boolean = interactor.isAuth()

    override fun onCleared() {
        disposable?.dispose()
    }

}
