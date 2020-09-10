package at.sunilson.vehicleDetails.di

import android.content.Context
import androidx.room.Room
import at.sunilson.authentication.di.AuthenticationModule
import at.sunilson.vehicleDetails.data.VehicleDetailsDatabase
import at.sunilson.vehicleDetails.data.VehicleDetailsService
import at.sunilson.vehiclecore.data.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object VehicleDetailsModule {

    @Provides
    @Singleton
    internal fun provideDatabase(@ApplicationContext context: Context) = Room
        .databaseBuilder(context, VehicleDetailsDatabase::class.java, "vehicleDetailsDatabase")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    internal fun provideVehicleDetailsDao(database: VehicleDetailsDatabase) =
        database.vehicleDetailsDao()

    @Provides
    @Singleton
    internal fun provideVehicleDetailsService(@Named(AuthenticationModule.AUTHENTICATED_HTTP_CLIENT) okHttpClient: OkHttpClient) =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(Constants.KAMEREON_BASE_URL)
            .build()
            .create(VehicleDetailsService::class.java)

}