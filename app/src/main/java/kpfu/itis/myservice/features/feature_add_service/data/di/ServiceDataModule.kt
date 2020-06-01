package kpfu.itis.myservice.features.feature_add_service.data.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import kpfu.itis.myservice.common.Mapper
import kpfu.itis.myservice.data.db.dao.ServiceDao
import kpfu.itis.myservice.features.feature_add_service.data.network.ServiceFirebase
import kpfu.itis.myservice.features.feature_add_service.data.network.ServiceFirebaseImpl
import kpfu.itis.myservice.features.feature_add_service.data.repository.ServiceRepository
import kpfu.itis.myservice.features.feature_add_service.data.repository.ServiceRepositoryImpl

@Module
class ServiceDataModule {

    @Provides
    @ServiceDataScope
    fun provideServiceFirebase(
        firebaseFirestore: FirebaseFirestore,
        mapper: Mapper
    ) : ServiceFirebase =
        ServiceFirebaseImpl(firebaseFirestore, mapper)

    @Provides
    @ServiceDataScope
    fun provideServiceRepository(
        dao: ServiceDao,
        firebase: ServiceFirebase
    ) : ServiceRepository = ServiceRepositoryImpl(dao, firebase)

}
