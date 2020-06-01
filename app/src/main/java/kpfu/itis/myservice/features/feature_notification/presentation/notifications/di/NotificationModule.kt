package kpfu.itis.myservice.features.feature_notification.presentation.notifications.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kpfu.itis.myservice.app.di.module.ViewModelModule
import kpfu.itis.myservice.app.di.key.ViewModelKey
import kpfu.itis.myservice.features.feature_notification.presentation.notifications.NotificationsViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
abstract class NotificationModule {

    @IntoMap
    @Binds
    @ViewModelKey(NotificationsViewModel::class)
    abstract fun bindAddSearchViewModel(
        notificationsViewModel: NotificationsViewModel
    ): ViewModel

}
