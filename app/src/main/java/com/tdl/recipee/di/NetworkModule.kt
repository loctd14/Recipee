package com.tdl.recipee.di

import android.content.Context
import com.tdl.recipee.support.ImageService
import com.tdl.recipee.support.NetworkClient
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by loc.ta on 2/22/2020.
 */
@Module
object NetworkModule {

    @JvmStatic
    @Provides
    @ApplicationScope
    fun provideImageOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(2L, TimeUnit.MINUTES)
            .readTimeout(2L, TimeUnit.MINUTES)
            .writeTimeout(2L, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .cache(getCache(context.cacheDir))
            .build()
    }

    @JvmStatic
    @Provides
    @ApplicationScope
    fun provideNetworkClient(okHttpClient: OkHttpClient): NetworkClient {
        return NetworkClient(okHttpClient)
    }

    @JvmStatic
    @Provides
    @ApplicationScope
    fun provideImageService(networkClient: NetworkClient): ImageService {
        return ImageService(networkClient)
    }

    private fun getCache(cacheDir: File): Cache {
        val httpCacheDirectory = File(cacheDir, "responses")
        val cacheSize = 10 * 1024 * 1024L
        return Cache(httpCacheDirectory, cacheSize)
    }
}