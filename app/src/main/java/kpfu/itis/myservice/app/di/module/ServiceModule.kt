package kpfu.itis.myservice.app.di.module

import dagger.Module
import dagger.Provides
import kpfu.itis.myservice.data.network.RestApiService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class ServiceModule {

    @Provides
    @Singleton
    fun provideCovidService(retrofit: Retrofit) : RestApiService =
        retrofit.create(RestApiService::class.java)

}
