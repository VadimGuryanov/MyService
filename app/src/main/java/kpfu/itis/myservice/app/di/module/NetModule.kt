package kpfu.itis.myservice.app.di.module

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        conventerFactory: GsonConverterFactory,
        callAdapterFactory: RxJava2CallAdapterFactory,
        @Named(TAG_BASE_URL) url: String
    ) : Retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(conventerFactory)
        .addCallAdapterFactory(callAdapterFactory)
        .baseUrl(url)
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @Named(TAG_AUTH) authInterceptor: Interceptor,
        @Named(TAG_LOGGING) loggingInterceptor: Interceptor
    ) : OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideConventerFactory() : GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideCallAdapterFactory() : RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    @Provides
    @Singleton
    @Named(TAG_AUTH)
    fun provideAuthInterceptor() = Interceptor { chain ->
        val newUrl = chain.request().url().newBuilder().build()
        val newRequest = chain.request().newBuilder().url(newUrl).build()
        chain.proceed(newRequest)
    }

    @Provides
    @Singleton
    @Named(TAG_LOGGING)
    fun provideHttpLogginInterceptor() : Interceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    @Named(TAG_BASE_URL)
//    fun provideBaseUrl() : String = BuildConfig.API_ENDPOINT
    fun provideBaseUrl() : String = ""

    companion object {
        private const val TAG_LOGGING = "tag_logging"
        private const val TAG_AUTH = "tag_auth"
        private const val TAG_BASE_URL = "tag_base_url"
    }

}
