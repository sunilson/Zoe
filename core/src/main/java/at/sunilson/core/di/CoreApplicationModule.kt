package at.sunilson.core.di

import android.app.Application
import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient

@Module
@InstallIn(ApplicationComponent::class)
object CoreApplicationModule {

    @Provides
    //TODO Use encrypted shared preferences
    fun provideSharedPreferences(application: Application) =
        application.getSharedPreferences("ZOE_APP", Context.MODE_PRIVATE)

    @Provides
    fun provideOkHTTPClient(application: Application) = OkHttpClient
        .Builder()
        .addInterceptor(
            ChuckerInterceptor(
                application.applicationContext,
                collector = ChuckerCollector(
                    application.applicationContext,
                    showNotification = true
                )
            )
        )
        .build()
}