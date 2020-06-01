package kpfu.itis.myservice.app.di.component

import dagger.BindsInstance
import dagger.Component
import kpfu.itis.myservice.app.App
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.app.di.module.*
import kpfu.itis.myservice.common.di.CommonModule
import kpfu.itis.myservice.features.feature_add_service.data.di.ServiceDataComponent
import kpfu.itis.myservice.features.feature_favorite_service.data.di.FavoritesDataComponent
import kpfu.itis.myservice.features.feature_notification.data.di.NotificationDataComponent
import kpfu.itis.myservice.features.feature_profile.data.di.ProfileDataComponent
import kpfu.itis.myservice.features.feature_search.data.di.SearchDataComponent
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ServiceModule::class,
        StorageModule::class,
        CommonModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder
        fun build(): AppComponent
    }

    fun plusProfileDataComponent() : ProfileDataComponent.Builder
    fun plusServiceDataComponent() : ServiceDataComponent.Builder
    fun plusSearchDataComponent() : SearchDataComponent.Builder
    fun plusFavoritesDataComponent() : FavoritesDataComponent.Builder
    fun plusNotificationDataComponent() : NotificationDataComponent.Builder
    fun inject(mainActivity: MainActivity)

}
