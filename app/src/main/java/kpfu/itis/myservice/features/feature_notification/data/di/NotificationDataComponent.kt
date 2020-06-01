package kpfu.itis.myservice.features.feature_notification.data.di

import dagger.Subcomponent
import kpfu.itis.myservice.features.feature_notification.domain.di.NotificationDomainComponent

@NotificationDataScope
@Subcomponent(modules = [NotificationDataModule::class])
interface NotificationDataComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build() : NotificationDataComponent
    }

    fun plusNotificationDomainComponent() : NotificationDomainComponent.Builder

}
