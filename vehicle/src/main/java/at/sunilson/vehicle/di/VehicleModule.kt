package at.sunilson.vehicle.di

import at.sunilson.authentication.di.AuthenticationModule.AUTHENTICATED_HTTP_CLIENT
import at.sunilson.vehicle.data.VehicleService
import at.sunilson.vehiclecore.data.Constants.KAMEREON_BASE_URL
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
object VehicleModule {

    @Provides
    @Singleton
    internal fun provideVehicleService(@Named(AUTHENTICATED_HTTP_CLIENT) okHttpClient: OkHttpClient) =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(KAMEREON_BASE_URL)
            .build()
            .create(VehicleService::class.java)
}
