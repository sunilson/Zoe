package at.sunilson.authentication.di

import android.content.Context
import at.sunilson.authentication.BuildConfig
import at.sunilson.authentication.data.AuthenticationInterceptor
import at.sunilson.authentication.data.GigyaService
import at.sunilson.authentication.data.KamereonService
import at.sunilson.authentication.domain.LogoutHandler
import at.sunilson.authentication.domain.LogoutHandlerImpl
import at.sunilson.networkingcore.di.NetworkingCoreModule.UNAUTHENTICATED_HTTP_CLIENT
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class AbstractAuthenticationModule {

    @Binds
    @Singleton
    abstract fun bindLogoutHandler(logoutHandlerImpl: LogoutHandlerImpl): LogoutHandler

}

@Module
@InstallIn(ApplicationComponent::class)
object AuthenticationModule {

    const val AUTHENTICATED_HTTP_CLIENT = "vehicleHttpClient"

    @Provides
    @Named(AUTHENTICATED_HTTP_CLIENT)
    internal fun provideAuthenticatedOkHttpClient(
        @ApplicationContext application: Context,
        authenticationInterceptor: AuthenticationInterceptor
    ) = OkHttpClient
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
            addInterceptor(authenticationInterceptor)
        }
        .build()


    @Provides
    @Singleton
    fun provideGigyaService(@Named(UNAUTHENTICATED_HTTP_CLIENT) okHttpClient: OkHttpClient) =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl("https://accounts.eu1.gigya.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GigyaService::class.java)

    @Provides
    @Singleton
    fun provideKamereonService(@Named(UNAUTHENTICATED_HTTP_CLIENT) okHttpClient: OkHttpClient) =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl("https://api-wired-prod-1-euw1.wrd-aws.com/commerce/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(KamereonService::class.java)
}