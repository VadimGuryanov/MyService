package kpfu.itis.myservice.app.di.component

import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Component
import kpfu.itis.myservice.app.App
import kpfu.itis.myservice.app.MainActivity
import kpfu.itis.myservice.app.di.module.*
import kpfu.itis.myservice.common.di.CommonModule
import kpfu.itis.myservice.features.feature_profile.data.di.ProfileDataComponent
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetModule::class,
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
    fun inject(mainActivity: MainActivity)

}
