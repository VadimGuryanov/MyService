package kpfu.itis.myservice.features.feature_message_service.service.worker

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.work.RxWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Notification
import kpfu.itis.myservice.features.feature_message_service.service.notification_manager.NotificationShower
import java.lang.Exception

class ListenMessageWorker (
    private val context: Context,
    private val workerParams: WorkerParameters
) : RxWorker(context, workerParams) {

//    override fun doWork(): Result {
//        Log.e("doWork", "work")
//        val id = inputData.getLong(ARG_USER_ID, -1)
//        val dbFirebase = FirebaseFirestore.getInstance()
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        dbFirebase
//            .collection(COLLECTION_USERS)
//            .document(id.toString())
//            .collection(COLLECTION_NOTIFICATION)
//            .addSnapshotListener { querySnapshot, exception ->
//                if (exception != null) {
//                    Log.e(this::class.java.name, exception.message.toString())
//                    return@addSnapshotListener
//                }
//                querySnapshot?.documentChanges?.forEach {
//                    when (it.type) {
//                        DocumentChange.Type.ADDED -> {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                NotificationShower.showChanel(notificationManager, applicationContext, mapper(it.document.data))
//                            } else {
//                                NotificationShower.show(notificationManager, applicationContext, mapper(it.document.data))
//                            }
//                        }
//                        else -> {
//                            Log.e(this::class.java.name + " type", it.type.toString())
//                        }
//                    }
//                } ?: Result.failure()
//            }
//        return Result.retry()
//    }

    fun mapper(map: Map<String, Any>) : Notification =
        map.let {
            Notification(
                mess_id = it["mess_id"].toString().toLong(),
                name = it["name"].toString(),
                lastName = it["lastName"].toString(),
                message = it["message"].toString(),
                data = it["data"].toString()
            )
        }

    companion object {
        const val TAG = "listenerMessage"
        const val ARG_USER_ID = "userId"
        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_NOTIFICATION = "notification"
    }

    override fun createWork(): Single<Result> {
        Log.e("doWork", "work")
        val id = inputData.getLong(ARG_USER_ID, -1)
        val dbFirebase = FirebaseFirestore.getInstance()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return Single.create { single ->
            dbFirebase
                .collection(COLLECTION_USERS)
                .document(id.toString())
                .collection(COLLECTION_NOTIFICATION)
                .addSnapshotListener { querySnapshot, exception ->
                    if (exception != null) {
                        single.onError(Exception("Проблемы"))
                        Log.e(this::class.java.name, exception.message.toString())
                        return@addSnapshotListener
                    }
                    querySnapshot?.documentChanges?.forEach {
                        when (it.type) {
                            DocumentChange.Type.ADDED -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    NotificationShower.showChanel(
                                        notificationManager,
                                        applicationContext,
                                        mapper(it.document.data)
                                    )
                                } else {
                                    NotificationShower.show(
                                        notificationManager,
                                        applicationContext,
                                        mapper(it.document.data)
                                    )
                                }
                                single.onSuccess(Result.success())
                            }
                            else -> {
                                Log.e(this::class.java.name + " type", it.type.toString())
                            }
                        }
                    } ?: single.onError(Exception("Проблемы"))
                }
        }
    }

}
