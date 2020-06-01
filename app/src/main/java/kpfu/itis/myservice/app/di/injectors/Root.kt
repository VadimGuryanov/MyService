package kpfu.itis.myservice.app.di.injectors

import kpfu.itis.myservice.app.App
import kpfu.itis.myservice.app.di.component.AppComponent
import kpfu.itis.myservice.app.di.component.DaggerAppComponent

object Root {

    lateinit var appComponent: AppComponent

    fun init(app: App) {
        appComponent = DaggerAppComponent.builder()
            .application(app)
            .build()
        ProfileInjector.init(appComponent)
        ServiceInjector.init(appComponent)
        SearchInjector.init(appComponent)
        FavoritesInjector.init(appComponent)
        NotificationInjector.init(appComponent)
    }

}
