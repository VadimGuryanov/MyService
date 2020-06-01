package kpfu.itis.myservice.features.feature_notification.presentation.notification

import android.util.Log
import android.widget.ImageView
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kpfu.itis.myservice.data.db.models.Notification
import kpfu.itis.myservice.features.feature_notification.domain.NotificationInteractor
import kpfu.itis.myservice.features.feature_notification.presentation.notification.di.NotificationDetailScope
import javax.inject.Inject

@NotificationDetailScope
class NotificationDetailViewModel @Inject constructor(
    private var interactor: NotificationInteractor
) : ViewModel() {

    private var disposable: Disposable? = null

    private lateinit var notificationLiveData: MutableLiveData<Result<Notification>>
    private lateinit var isRead: MutableLiveData<Result<Boolean>>

    @MainThread
    fun getNotifications(id: Long) : LiveData<Result<Notification>> {
        notificationLiveData = MutableLiveData()
        disposable = interactor
            .getNotification(id)
            .subscribeBy(
                onSuccess = {
                    val sup = notificationLiveData
                    sup.postValue(Result.success(it))
                    notificationLiveData = sup
                },
                onError = {
                    val sup = notificationLiveData
                    sup.postValue(Result.failure(it))
                    Log.e("notificaton", it.message.toString())
                    notificationLiveData = sup
                }
            )
        return notificationLiveData
    }

    @MainThread
    fun read(id: Long) : LiveData<Result<Boolean>> {
        isRead = MutableLiveData()
        disposable = interactor
            .read(id)
            .subscribeBy(
                onComplete = {
                    val sup = isRead
                    sup.postValue(Result.success(true))
                    isRead = sup
                },
                onError = {
                    val sup = isRead
                    sup.postValue(Result.failure(it))
                    isRead = sup
                }
            )
        return isRead
    }

    fun isAuth() : Boolean = interactor.isAuth()

    override fun onCleared() {
        disposable?.dispose()
    }

}
