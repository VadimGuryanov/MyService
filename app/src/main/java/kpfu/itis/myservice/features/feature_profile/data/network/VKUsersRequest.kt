package kpfu.itis.myservice.features.feature_profile.data.network

import com.google.gson.Gson
import com.vk.api.sdk.*
import com.vk.api.sdk.requests.VKRequest
import com.vk.api.sdk.utils.log.DefaultApiLogger
import com.vk.api.sdk.utils.log.Logger
import kpfu.itis.myservice.features.feature_profile.data.model.VKUser
import org.json.JSONObject
import java.util.concurrent.TimeUnit


class VKUsersRequest: VKRequest<List<VKUser>> {
    constructor(uids: IntArray = intArrayOf()): super("users.get") {
        if (uids.isNotEmpty()) {
            if (uids[0] == -1) {
                return
            }
            addParam("user_ids", uids.joinToString(","))
        }
        addParam("fields", "$FIELD_PHOTO,$FIELD_CITY,$FIELD_CONTACTS,$FIELD_UNIVERSITY")
        addParam("lang", "ru")
    }

    override fun onExecute(manager: VKApiManager): List<VKUser> {
        val config = manager.config
        if (!params.containsKey("lang")) {
            params["lang"] = config.lang
        }
        params["device_id"] = config.deviceId.value
        params["v"] = config.version

        return manager.execute(VKMethodCall.Builder()
            .args(params)
            .method(method)
            .version(config.version)
            .build(), this)
    }

    override fun parse(r: JSONObject): List<VKUser> {
        val users = r.getJSONArray("response")
        val result = ArrayList<VKUser>()
        for (i in 0 until users.length()) {
            result.add(parseUser(users.getJSONObject(i)))
        }
        return result
    }

    fun parseUser(json: JSONObject) =
        Gson().fromJson(json.toString(), VKUser::class.java)

    companion object {
        private const val FIELD_CITY = "city"
        private const val FIELD_PHOTO = "photo_200_orig"
        private const val FIELD_UNIVERSITY = "universities"
        private const val FIELD_CONTACTS = "contacts"
    }
}