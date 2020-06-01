package kpfu.itis.myservice.common

import kpfu.itis.myservice.data.db.models.Favorite
import kpfu.itis.myservice.data.db.models.Service
import kpfu.itis.myservice.data.db.models.User
import kpfu.itis.myservice.features.feature_profile.data.model.VKUser

class Mapper {

    fun map(userLocal: User, vkUser: VKUser) : User =
        userLocal.also {
            vkUser.apply {
                it.vk_id = id
                it.city = city?.title
                it.name = first_name
                it.lastName = last_name
                it.faculty = universities?.get(0)?.faculty_name
                it.university = universities?.get(0)?.name
                it.mobilePhone = mobile_phone
                it.photoURL = photo_200_orig
            }
        }

    fun map(user: VKUser) : User =
        user.run {
            User(
                id,
                first_name,
                last_name,
                city?.title,
                photo_200_orig,
                mobile_phone,
                universities?.get(0)?.name,
                universities?.get(0)?.faculty_name,
                null,
                null,
                null
            )
        }

    fun map(user: User) = hashMapOf(
        "vk_id" to user.vk_id,
        "name" to user.name,
        "lastName" to user.lastName,
        "photoURL" to user.photoURL,
        "city" to user.city,
        "university" to user.university,
        "faculty" to user.faculty,
        "job" to user.job,
        "mobilePhone" to user.mobilePhone,
        "socialUrl" to user.socialUrl,
        "description" to user.description
    )

    fun map(service: Service) = hashMapOf(
        "ser_id" to service.ser_id,
        "user_id" to service.user_id,
        "city" to service.city,
        "title" to service.title,
        "description" to service.description,
        "specialty" to service.specialty,
        "mobilePhone" to service.mobilePhone,
        "socialUrl" to service.socialUrl,
        "cost" to service.cost,
        "date" to service.date,
        "experience" to service.experience,
        "currancy" to  service.currancy
    )

    fun map(map: MutableMap<String, Any>?) : User? =
        map?.let {
            User(
                map["vk_id"] as Long,
                map["name"] as String,
                map["lastName"] as String,
                map["city"] as String?,
                map["photoURL"] as String,
                map["mobilePhone"] as String?,
                map["university"] as String?,
                map["faculty"] as String?,
                map["job"] as String?,
                map["description"] as String?,
                map["socialUrl"] as String?
            )
        }

    fun mapToFavorite(service: Service) : Favorite =
        service.run {
            Favorite(
                ser_id = ser_id,
                user_id = user_id,
                title = title,
                city = city,
                mobilePhone = mobilePhone,
                specialty = specialty,
                description = description,
                socialUrl = socialUrl,
                cost = cost,
                currancy = currancy,
                date = date,
                experience = experience,
                name = name,
                lastName = lastName
            )
        }

}
