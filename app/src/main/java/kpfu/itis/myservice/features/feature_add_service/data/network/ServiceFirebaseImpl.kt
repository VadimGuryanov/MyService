package kpfu.itis.myservice.features.feature_add_service.data.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import de.aaronoe.rxfirestore.getCompletable
import de.aaronoe.rxfirestore.getSingle
import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.common.Mapper
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User
import kpfu.itis.myservice.features.feature_add_service.data.di.ServiceDataScope
import javax.inject.Inject

@ServiceDataScope
class ServiceFirebaseImpl @Inject constructor(
    var dbFirebase: FirebaseFirestore,
    var mapper: Mapper
): ServiceFirebase {

    companion object {
        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_SERVICES = "services"
    }

    override fun getServices(id: Long): Single<List<Service>> =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(id.toString())
            .collection(COLLECTION_SERVICES)
            .getSingle()

    override fun addService(id: Long, service: Service) : Completable =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(id.toString())
            .collection(COLLECTION_SERVICES)
            .document(service.ser_id.toString())
            .set(service, SetOptions.merge())
            .getCompletable()

    override fun updateService(id: Long, service: Service): Completable =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(id.toString())
            .collection(COLLECTION_SERVICES)
            .document(service.ser_id.toString())
            .update(mapper.map(service))
            .getCompletable()

    override fun getService(id: Long, serviceId: Long): Single<Service> =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(id.toString())
            .collection(COLLECTION_SERVICES)
            .document(serviceId.toString())
            .getSingle()

    override fun getUser(id: Long): Single<User> =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(id.toString())
            .getSingle()

    override fun getCount() : Single<Int> =
        Single.create { single ->
            dbFirebase
                .collectionGroup(COLLECTION_SERVICES)
                .get()
                .addOnSuccessListener {
                    if (it.isEmpty) single.onError(Exception())
                    single.onSuccess(
                        it.documents
                            .map {doc ->  doc.id.toInt() }
                            .max() ?: -1
                    )
                }
                .addOnFailureListener {
                    single.onError(it)
                }
        }

    override fun deleteService(id: Long, serviceId: Long): Completable =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(id.toString())
            .collection(COLLECTION_SERVICES)
            .document(serviceId.toString())
            .delete()
            .getCompletable()

}
