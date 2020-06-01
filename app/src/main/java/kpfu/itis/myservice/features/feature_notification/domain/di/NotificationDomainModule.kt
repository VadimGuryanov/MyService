package kpfu.itis.myservice.features.feature_notification.domain.di

import dagger.Module
import dagger.Provides
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.features.feature_notification.data.repository.NotificationRepository
import kpfu.itis.myservice.features.feature_notification.domain.NotificationInteractor
import kpfu.itis.myservice.features.feature_notification.domain.NotificationInteractorImpl

@Module
class NotificationDomainModule {

    @Provides
    @NotificationDomainScope
    fun provideNotificationInteractor(
        repository: NotificationRepository,
        helper: HelperSharedPreferences
    ) : NotificationInteractor = NotificationInteractorImpl(repository, helper)

}
