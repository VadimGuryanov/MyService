package kpfu.itis.myservice.features.feature_notification.presentation.notification.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kpfu.itis.myservice.app.di.key.ViewModelKey
import kpfu.itis.myservice.app.di.module.ViewModelModule
import kpfu.itis.myservice.features.feature_notification.presentation.notification.NotificationDetailViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
abstract class NotificationViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(NotificationDetailViewModel::class)
    abstract fun bindNotificationDetailViewModel(
        notificationDetailViewModel: NotificationDetailViewModel
    ): ViewModel

}