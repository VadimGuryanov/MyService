package kpfu.itis.myservice.features.feature_search.data.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import de.aaronoe.rxfirestore.getCompletable
import de.aaronoe.rxfirestore.getSingle
import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Notification
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User
import kpfu.itis.myservice.features.feature_search.data.di.SearchDataScope
import javax.inject.Inject

@SearchDataScope
class SearchFirebaseImpl @Inject constructor(
    private var dbFirebase: FirebaseFirestore
) : SearchFirebase {

    companion object {
        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_SERVICES = "services"
        private const val COLLECTION_NOTIFICATION = "notification"
        private const val QUERY_TITLE = "title"
    }

    override fun getServices(): Single<List<Service>> =
        dbFirebase
            .collectionGroup(COLLECTION_SERVICES)
            .getSingle()

    override fun getServicesByTitle(query: String): Single<List<Service>> =
        dbFirebase
            .collectionGroup(COLLECTION_SERVICES)
            .whereGreaterThanOrEqualTo(QUERY_TITLE, query)
            .getSingle()

    override fun getService(id: Long, userId: Long): Single<Service> =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(userId.toString())
            .collection(COLLECTION_SERVICES)
            .document(id.toString())
            .getSingle()

    override fun sendMessage(message: Notification): Completable =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(message.to_user_id.toString())
            .collection(COLLECTION_NOTIFICATION)
            .document(message.mess_id.toString())
            .set(message, SetOptions.merge())
            .getCompletable()

    override fun getCount(): Single<Long> =
        Single.create { single ->
            dbFirebase
                .collectionGroup(COLLECTION_NOTIFICATION)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) single.onError(Exception())
                    single.onSuccess(
                        it.documents
                            .map {doc ->  doc.id.toLong() }
                            .max() ?: -1
                    )
                }
                .addOnFailureListener {
                    single.onError(it)
                }
        }

    override fun getUser(id: Long): Single<User> =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(id.toString())
            .getSingle()

}
