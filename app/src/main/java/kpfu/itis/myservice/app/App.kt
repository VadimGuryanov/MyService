package kpfu.itis.myservice.app

import android.app.Application
import kpfu.itis.myservice.app.di.injectors.Root
import kpfu.itis.myservice.common.HelperSharedPreferences
import javax.inject.Inject

class App : Application() {

    @Inject
    lateinit var helper : HelperSharedPreferences

    override fun onCreate() {
        super.onCreate()
        Root.init(this)
    }

}
