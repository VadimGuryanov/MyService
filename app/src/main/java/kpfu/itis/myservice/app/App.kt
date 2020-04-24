package kpfu.itis.myservice.app

import android.app.Application
//import com.vk.sdk.VKSdk
import kpfu.itis.myservice.app.di.Injector
import kpfu.itis.myservice.common.HelperSharedPreferences
import javax.inject.Inject

class App : Application() {

    @Inject
    lateinit var helper : HelperSharedPreferences

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
    }

}