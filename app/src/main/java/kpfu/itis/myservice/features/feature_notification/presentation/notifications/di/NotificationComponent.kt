package kpfu.itis.myservice.features.feature_notification.presentation.notifications.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_notification.presentation.notifications.NotificationsFragment

@NotificationScope
@Subcomponent(modules = [NotificationModule::class])
interface NotificationComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : NotificationComponent
    }

    fun inject(notificationsFragment: NotificationsFragment)

}
