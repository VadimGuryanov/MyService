package kpfu.itis.myservice.features.feature_profile.domain

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import kpfu.itis.myservice.data.db.models.UserLocal

interface ProfileInteractor {

    fun auth(): Single<Boolean>

    fun getUser(id : Long) : Flowable<UserLocal>

    fun addDescription(description: String?): Single<Boolean>

}
