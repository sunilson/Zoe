package at.sunilson.networkingcore.di

import android.content.Context
import at.sunilson.networkingcore.BuildConfig
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Named

@InstallIn(ApplicationComponent::class)
@Module
object NetworkingCoreModule {

    const val UNAUTHENTICATED_HTTP_CLIENT = "unauthenticatedHttpClient"

    @Provides
    @Named(UNAUTHENTICATED_HTTP_CLIENT)
    fun provideOkHTTPClient(@ApplicationContext application: Context) = OkHttpClient
        .Builder()
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    ChuckerInterceptor(
                        application,
                        collector = ChuckerCollector(application, showNotification = true)
                    )
                )
            }
            callTimeout(30, TimeUnit.SECONDS)
        }
        .build()

}