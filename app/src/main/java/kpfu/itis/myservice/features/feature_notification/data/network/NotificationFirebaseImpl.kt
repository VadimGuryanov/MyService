package kpfu.itis.myservice.features.feature_notification.data.network

import com.google.firebase.firestore.FirebaseFirestore
import de.aaronoe.rxfirestore.getCompletable
import de.aaronoe.rxfirestore.getSingle
import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Notification
import kpfu.itis.myservice.features.feature_notification.data.di.NotificationDataScope
import javax.inject.Inject

@NotificationDataScope
class NotificationFirebaseImpl @Inject constructor(
    private var dbFirebase: FirebaseFirestore
) : NotificationFirebase {

    companion object {
        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_NOTIFICATION = "notification"
    }

    override fun getNotifications(id: Long): Single<List<Notification>> =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(id.toString())
            .collection(COLLECTION_NOTIFICATION)
            .orderBy("read")
            .getSingle()

    override fun read(id: Long, userId: Long): Completable =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(userId.toString())
            .collection(COLLECTION_NOTIFICATION)
            .document(id.toString())
            .update("read", true)
            .getCompletable()

    override fun getNotification(id: Long, userId: Long): Single<Notification> =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(userId.toString())
            .collection(COLLECTION_NOTIFICATION)
            .document(id.toString())
            .getSingle()

}
