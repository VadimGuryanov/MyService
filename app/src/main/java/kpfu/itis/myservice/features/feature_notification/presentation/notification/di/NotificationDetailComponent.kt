package kpfu.itis.myservice.features.feature_notification.presentation.notification.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_notification.presentation.notification.NotificationDetailFragment

@NotificationDetailScope
@Subcomponent(modules = [NotificationViewModelModule::class])
interface NotificationDetailComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : NotificationDetailComponent
    }

    fun inject(notificationDetailFragment: NotificationDetailFragment)

}
