package kpfu.itis.myservice.features.feature_profile.data.network

import io.reactivex.Completable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User

interface UserFirebase {

    fun getUser(id: Long) : Single<User>
    fun saveUser(user: User): Completable
    fun deleteUser(id: Long) : Completable
    fun updateUser(user: User) : Completable
    fun updateDescription(id: Long, description : String) : Completable
    fun deleteDescription(id: Long) : Completable
    fun getServices(id: Long): Single<List<Service>>
    fun getService(id: Long, userId: Long): Single<Service>

}
