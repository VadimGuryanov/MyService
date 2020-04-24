package kpfu.itis.myservice.features.feature_profile.domain.di

import dagger.Module
import dagger.Provides
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.features.feature_profile.data.repository.ProfileRepository
import kpfu.itis.myservice.features.feature_profile.domain.ProfileInteractor
import kpfu.itis.myservice.features.feature_profile.domain.ProfileInteractorImpl

@Module
class ProfileDomainModule {

    @Provides
    @ProfileDomainScope
    fun provideInteractor(
        repository: ProfileRepository,
        helperSharedPreference: HelperSharedPreferences
    ): ProfileInteractor =
        ProfileInteractorImpl(repository, helperSharedPreference)

}
