package at.sunilson.vehicleMap.di

import at.sunilson.networkingcore.di.NetworkingCoreDebugModule
import at.sunilson.networkingcore.di.NetworkingCoreModule.UNAUTHENTICATED_HTTP_CLIENT
import at.sunilson.vehicleMap.data.MapsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object VehicleMapModule {

    @Provides
    @Singleton
    internal fun provideMapsService(@Named(UNAUTHENTICATED_HTTP_CLIENT) client: OkHttpClient): MapsService {
        return Retrofit
            .Builder()
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://www.google.at")
            .build()
            .create(MapsService::class.java)
    }
}