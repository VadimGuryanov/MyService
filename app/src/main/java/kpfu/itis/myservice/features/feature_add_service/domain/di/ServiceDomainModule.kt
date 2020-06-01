package kpfu.itis.myservice.features.feature_add_service.domain.di

import dagger.Module
import dagger.Provides
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.features.feature_add_service.data.repository.ServiceRepository
import kpfu.itis.myservice.features.feature_add_service.domain.ServiceInteractor
import kpfu.itis.myservice.features.feature_add_service.domain.ServiceInteractorImpl

@Module
class ServiceDomainModule {

    @Provides
    @ServiceDomainScope
    fun provideServiceInteractor(
        repository: ServiceRepository,
        helper: HelperSharedPreferences
    ) : ServiceInteractor = ServiceInteractorImpl(repository, helper)

}
