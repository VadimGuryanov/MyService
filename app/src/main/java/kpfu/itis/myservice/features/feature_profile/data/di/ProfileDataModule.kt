package kpfu.itis.myservice.features.feature_profile.data.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import kpfu.itis.myservice.common.Mapper
import kpfu.itis.myservice.data.db.dao.FavoriteDao
import kpfu.itis.myservice.data.db.dao.UserDao
import kpfu.itis.myservice.features.feature_profile.data.network.UserFirebase
import kpfu.itis.myservice.features.feature_profile.data.network.UserFirebaseImpl
import kpfu.itis.myservice.features.feature_profile.data.network.VKUsersRequest
import kpfu.itis.myservice.features.feature_profile.data.repository.ProfileRepository
import kpfu.itis.myservice.features.feature_profile.data.repository.ProfileRepositoryImpl

@Module
class ProfileDataModule {

    @Provides
    @ProfileDataScope
    fun provideRepository(
        dao: UserDao,
        favoriteDao: FavoriteDao,
        userFirebaseApi: UserFirebase,
        mapper: Mapper
    ) : ProfileRepository = ProfileRepositoryImpl(dao, favoriteDao, userFirebaseApi, mapper)

    @Provides
    @ProfileDataScope
    fun provideVKRequests() : VKUsersRequest = VKUsersRequest()

    @Provides
    @ProfileDataScope
    fun provideUserFirebaseApi(
        firebaseFirestore: FirebaseFirestore,
        mapper: Mapper
    ) : UserFirebase = UserFirebaseImpl(firebaseFirestore, mapper)

}
