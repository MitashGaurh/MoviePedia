package com.mitash.moviepedia.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.mitash.moviepedia.api.EnumConverterFactory
import com.mitash.moviepedia.api.MoviePediaService
import com.mitash.moviepedia.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * Created by Mitash Gaurh on 7/19/2018.
 */
@Module
class ApiModule {

    companion object {
        private const val BASE_URL: String = "https://api.themoviedb.org/"
        private const val API_KEY: String = "965fc68f9434e975756f7ae709432ab6"
    }

    @Provides
    @Singleton
    fun provideStethoInterceptor(): StethoInterceptor {
        return StethoInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(stethoInterceptor: StethoInterceptor,
                            authenticationInterceptor: Interceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
                .addNetworkInterceptor(stethoInterceptor)
                .addInterceptor(authenticationInterceptor)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @Singleton
    fun provideAuthenticationInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url()

            val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .build()

            val requestBuilder = original.newBuilder()
                    .url(url)

            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideJacksonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gsonConverterFactory: GsonConverterFactory,
                        okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .addConverterFactory(EnumConverterFactory())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): MoviePediaService {
        return retrofit.create(MoviePediaService::class.java)
    }
}