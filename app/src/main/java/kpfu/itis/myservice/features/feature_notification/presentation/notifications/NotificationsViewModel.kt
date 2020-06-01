package kpfu.itis.myservice.features.feature_notification.presentation.notifications

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
import kpfu.itis.myservice.features.feature_notification.presentation.notifications.di.NotificationScope
import javax.inject.Inject

@NotificationScope
class NotificationsViewModel @Inject constructor(
    private var interactor: NotificationInteractor
) : ViewModel() {

    private var disposable: Disposable? = null

    private lateinit var notificationLiveData: MutableLiveData<Result<List<Notification>>>
    private lateinit var isLoading: MutableLiveData<Boolean>
    private lateinit var isRead: MutableLiveData<Result<Boolean>>

    @MainThread
    fun getNotifications() : LiveData<Result<List<Notification>>> {
        notificationLiveData = MutableLiveData()
        disposable = interactor
            .getNotifications()
            .doOnSubscribe{isLoading.postValue(true)}
            .doAfterTerminate{isLoading.postValue(false)}
            .subscribeBy(
                onSuccess = {
                    val sup = notificationLiveData
                    sup.postValue(Result.success(it))
                    notificationLiveData = sup
                },
                onError = {
                    val sup = notificationLiveData
                    sup.postValue(Result.failure(it))
                    notificationLiveData = sup
                }
            )
        return notificationLiveData
    }

    @MainThread
    fun isLoading() : LiveData<Boolean> {
        isLoading = MutableLiveData()
        return isLoading
    }

    fun isAuth() : Boolean = interactor.isAuth()

    override fun onCleared() {
        disposable?.dispose()
    }

    fun downloadPhoto(view: ImageView, url: String) {
        Glide
            .with(view.context)
            .load(url)
            .centerCrop()
            .into(view)
    }

}
