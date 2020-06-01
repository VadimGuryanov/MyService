package kpfu.itis.myservice.features.feature_profile.data.model

data class VKUser(
    val id: Long,
    val first_name: String,
    val last_name: String,
    val city: City?,
    val photo_200_orig: String,
    val home_phone: String?,
    val mobile_phone: String?,
    val verified: Int,
    val universities: List<University>?
)
