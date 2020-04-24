package kpfu.itis.myservice.features.feature_profile.data.repository

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.UserLocal
import kpfu.itis.myservice.features.feature_profile.data.model.VKUser

interface ProfileRepository {

    fun getUser(id : Long) : Flowable<UserLocal>

    fun authUser()

    fun addDescription(description: String?)

//    fun updateUser(userLocal: UserLocal) : UserLocal

}