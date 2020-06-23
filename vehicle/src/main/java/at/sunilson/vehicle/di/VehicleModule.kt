package at.sunilson.vehicle.di

import at.sunilson.authentication.di.AuthenticationModule.AUTHENTICATED_HTTP_CLIENT
import at.sunilson.vehicle.data.VehicleRepositoryImpl
import at.sunilson.vehicle.data.VehicleService
import at.sunilson.vehicle.domain.VehicleRepository
import dagger.Binds
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
abstract class VehicleBindingModule {
    @Binds
    @Singleton
    internal abstract fun bindVehicleRepository(vehicleRepositoryImpl: VehicleRepositoryImpl): VehicleRepository
}

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
            .baseUrl("https://api-wired-prod-1-euw1.wrd-aws.com/commerce/v1/")
            .build()
            .create(VehicleService::class.java)

}