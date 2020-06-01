package kpfu.itis.myservice.features.feature_notification.data.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import kpfu.itis.myservice.data.db.dao.NotificationDao
import kpfu.itis.myservice.features.feature_notification.data.network.NotificationFirebase
import kpfu.itis.myservice.features.feature_notification.data.network.NotificationFirebaseImpl
import kpfu.itis.myservice.features.feature_notification.data.repository.NotificationRepository
import kpfu.itis.myservice.features.feature_notification.data.repository.NotificationRepositoryImpl

@Module
class NotificationDataModule {

    @Provides
    @NotificationDataScope
    fun provideNotificationFirebase(
        firebaseFirestore: FirebaseFirestore
    ) : NotificationFirebase =
        NotificationFirebaseImpl(firebaseFirestore)

    @Provides
    @NotificationDataScope
    fun provideNotificationRepository(
        dao: NotificationDao,
        firebase: NotificationFirebase
    ) : NotificationRepository = NotificationRepositoryImpl(dao, firebase)

}
