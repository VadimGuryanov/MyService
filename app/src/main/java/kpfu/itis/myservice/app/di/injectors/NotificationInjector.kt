package kpfu.itis.myservice.app.di.injectors

import kpfu.itis.myservice.app.di.component.AppComponent
import kpfu.itis.myservice.features.feature_notification.data.di.NotificationDataComponent
import kpfu.itis.myservice.features.feature_notification.domain.di.NotificationDomainComponent
import kpfu.itis.myservice.features.feature_notification.presentation.notification.di.NotificationDetailComponent
import kpfu.itis.myservice.features.feature_notification.presentation.notifications.di.NotificationComponent

object NotificationInjector {

    lateinit var appComponent: AppComponent
    private var notificationDataComponent: NotificationDataComponent? = null
    private var notificationDomainComponent : NotificationDomainComponent? = null
    private var notificationComponent: NotificationComponent? = null
    private var notificationDetailComponent: NotificationDetailComponent? = null

    fun init(app: AppComponent) {
        appComponent = app
    }

    fun plusNotificationDataComponent(): NotificationDataComponent = notificationDataComponent
        ?: appComponent
            .plusNotificationDataComponent()
            .build().also {
                notificationDataComponent = it
            }

    fun clearNotificationDataComponent() {
        notificationDataComponent = null
    }

    fun plusNotificationDomainComponent(): NotificationDomainComponent = notificationDomainComponent
        ?: plusNotificationDataComponent()
            .plusNotificationDomainComponent()
            .build().also {
                notificationDomainComponent = it
            }

    fun clearNotificationDomainComponent() {
        notificationDomainComponent = null
    }

    fun plusNotificationComponent(): NotificationComponent = notificationComponent
        ?: plusNotificationDomainComponent()
            .plusNotificationComponent()
            .build().also {
                notificationComponent = it
            }

    fun clearNotificationComponent() {
        notificationComponent = null
    }

    fun plusNotificationDetailComponent(): NotificationDetailComponent = notificationDetailComponent
        ?: plusNotificationDomainComponent()
            .plusNotificationDetailComponent()
            .build().also {
                notificationDetailComponent = it
            }

    fun clearNotificationDetailComponent() {
        notificationDetailComponent = null
    }

}
