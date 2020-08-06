package at.sunilson.chargeSchedule.di

import at.sunilson.authentication.di.AuthenticationModule
import at.sunilson.chargeSchedule.data.ChargeScheduleService
import at.sunilson.vehiclecore.data.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object ChargeScheduleModule {

    @Provides
    @Singleton
    internal fun provideChargeScheduleService(@Named(AuthenticationModule.AUTHENTICATED_HTTP_CLIENT) okHttpClient: OkHttpClient) =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(Constants.KAMEREON_BASE_URL)
            .build()
            .create(ChargeScheduleService::class.java)

}