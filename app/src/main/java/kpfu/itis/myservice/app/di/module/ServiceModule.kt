package kpfu.itis.myservice.app.di.module

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ServiceModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore() : FirebaseFirestore = FirebaseFirestore.getInstance().apply {
        firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
    }

}
