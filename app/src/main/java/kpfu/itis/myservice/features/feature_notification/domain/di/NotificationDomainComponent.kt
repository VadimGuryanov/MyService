package kpfu.itis.myservice.features.feature_notification.domain.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_notification.presentation.notification.di.NotificationDetailComponent
import kpfu.itis.myservice.features.feature_notification.presentation.notifications.di.NotificationComponent

@NotificationDomainScope
@Subcomponent(modules = [NotificationDomainModule::class])
interface NotificationDomainComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : NotificationDomainComponent
    }

    fun plusNotificationComponent() : NotificationComponent.Builder
    fun plusNotificationDetailComponent() : NotificationDetailComponent.Builder

}
