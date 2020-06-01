package kpfu.itis.myservice.features.feature_profile.presentation.edit.dto

data class UserDto (
    var name: String = "",
    var lastName: String = "",
    var photoUrl: String = "",
    var city: String? = null,
    var socialUrl: String? = null,
    var mobilePhone: String? = null,
    var university: String? = null,
    var faculty: String? = null,
    var job: String? = null,
    var description: String? = null
)
