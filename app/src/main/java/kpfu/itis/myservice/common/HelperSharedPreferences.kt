package kpfu.itis.myservice.common

import android.content.SharedPreferences

class HelperSharedPreferences(
    private var sharedPreferences: SharedPreferences
) {

    companion object {
        private const val TAG_ID = "tag_id"
        private const val TAG_SESSION = "tag_auth_session"
    }

    fun editId(data: String) =
        sharedPreferences.edit()
            .putString(TAG_ID, data)
            .apply()

    fun readID() : String? = sharedPreferences.getString(TAG_ID, null)

    fun deleteID() =
        sharedPreferences.edit()
            .putString(TAG_ID, null)
            .apply()

    fun editSession(data: String) =
        sharedPreferences.edit()
            .putString(TAG_SESSION, data)
            .apply()

    fun readSession() : String? = sharedPreferences.getString(TAG_SESSION, null)

    fun deleteSession() =
        sharedPreferences.edit()
            .putString(TAG_SESSION, null)
            .apply()

    fun clear() {
        deleteID()
        deleteSession()
    }

}
