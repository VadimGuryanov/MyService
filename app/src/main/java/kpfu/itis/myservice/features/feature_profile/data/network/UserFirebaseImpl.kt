package kpfu.itis.myservice.features.feature_profile.data.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import de.aaronoe.rxfirestore.getCompletable
import de.aaronoe.rxfirestore.getSingle
import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.common.Mapper
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User
import kpfu.itis.myservice.features.feature_profile.data.di.ProfileDataScope
import javax.inject.Inject

@ProfileDataScope
class UserFirebaseImpl @Inject constructor(
    var dbFirebase: FirebaseFirestore,
    var mapper: Mapper
) : UserFirebase {

    companion object {
        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_SERVICES = "services"
    }

    override fun getUser(id: Long): Single<User> =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(id.toString())
            .getSingle()

    override fun saveUser(user: User) : Completable =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(user.vk_id.toString())
            .set(user, SetOptions.merge())
            .getCompletable()

    override fun deleteUser(id: Long) : Completable =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(id.toString())
            .delete()
            .getCompletable()

    override fun updateUser(user: User) : Completable =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(user.vk_id.toString())
            .update(mapper.map(user))
            .getCompletable()

    override fun updateDescription(id: Long, description: String): Completable =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(id.toString())
            .update("description", description)
            .getCompletable()

    override fun deleteDescription(id: Long): Completable =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(id.toString())
            .update("description", null)
            .getCompletable()

    override fun getServices(id: Long): Single<List<Service>> =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(id.toString())
            .collection(COLLECTION_SERVICES)
            .getSingle()

    override fun getService(id: Long, userId: Long): Single<Service> =
        dbFirebase
            .collection(COLLECTION_USERS)
            .document(userId.toString())
            .collection(COLLECTION_SERVICES)
            .document(id.toString())
            .getSingle()

}
