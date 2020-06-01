package kpfu.itis.myservice.features.feature_search.data.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import kpfu.itis.myservice.common.Mapper
import kpfu.itis.myservice.data.db.dao.FavoriteDao
import kpfu.itis.myservice.features.feature_search.data.network.SearchFirebase
import kpfu.itis.myservice.features.feature_search.data.network.SearchFirebaseImpl
import kpfu.itis.myservice.features.feature_search.data.repository.SearchRepository
import kpfu.itis.myservice.features.feature_search.data.repository.SearchRepositoryImpl

@Module
class SearchDataModule {

    @Provides
    @SearchDataScope
    fun provideSearchFirebase(
        firebase: FirebaseFirestore
    ) : SearchFirebase =
        SearchFirebaseImpl(firebase)

    @Provides
    @SearchDataScope
    fun provideSearchRepository(
        firebase: SearchFirebase,
        favoriteDao: FavoriteDao,
        mapper: Mapper
    ) : SearchRepository = SearchRepositoryImpl(firebase, favoriteDao, mapper)

}
