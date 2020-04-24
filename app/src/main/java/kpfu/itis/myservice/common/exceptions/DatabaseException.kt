package kpfu.itis.myservice.common.exceptions

import java.lang.Exception

data class DatabaseException(override var message : String) : Exception(message)