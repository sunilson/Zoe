package at.sunilson.networkingcore.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import javax.inject.Named

@InstallIn(ApplicationComponent::class)
@Module
object NetworkingCoreModule {

    const val UNAUTHENTICATED_HTTP_CLIENT = "unauthenticatedHttpClient"

    @Provides
    @Named(UNAUTHENTICATED_HTTP_CLIENT)
    fun provideOkHTTPClient(@ApplicationContext application: Context) = OkHttpClient
        .Builder()
        .addInterceptor(
            ChuckerInterceptor(
                application,
                collector = ChuckerCollector(application, showNotification = true)
            )
        )
        .build()

}