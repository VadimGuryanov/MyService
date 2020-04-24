package kpfu.itis.myservice.features.feature_profile.data.di

import dagger.Module
import dagger.Provides
import kpfu.itis.myservice.common.HelperSharedPreferences
import kpfu.itis.myservice.data.db.dao.UserDao
import kpfu.itis.myservice.features.feature_profile.data.network.VKUsersRequest
import kpfu.itis.myservice.features.feature_profile.data.repository.ProfileRepository
import kpfu.itis.myservice.features.feature_profile.data.repository.ProfileRepositoryImpl

@Module
class ProfileDataModule {

    @Provides
    @ProfileDataScope
    fun provideRepository(
        dao: UserDao,
        helper: HelperSharedPreferences
    ) : ProfileRepository = ProfileRepositoryImpl(dao, helper)

    @Provides
    @ProfileDataScope
    fun provideVKRequests() : VKUsersRequest = VKUsersRequest()

}
