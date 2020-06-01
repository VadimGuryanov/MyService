package kpfu.itis.myservice.common.exceptions

import java.lang.Exception

data class NetworkException(override var message : String) : Exception(message)
