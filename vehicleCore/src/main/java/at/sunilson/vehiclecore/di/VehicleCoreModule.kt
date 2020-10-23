package at.sunilson.vehiclecore.di

import android.content.Context
import androidx.room.Room
import at.sunilson.authentication.di.AuthenticationModule
import at.sunilson.vehiclecore.data.Constants.KAMEREON_BASE_URL
import at.sunilson.vehiclecore.data.VehicleCoreRepositoryImpl
import at.sunilson.vehiclecore.data.VehicleCoreService
import at.sunilson.vehiclecore.data.VehicleDatabase
import at.sunilson.vehiclecore.data.ZoeDatabase
import at.sunilson.vehiclecore.data.models.MIGRATION_6_7
import at.sunilson.vehiclecore.domain.VehicleCoreRepository
import dagger.Binds
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
abstract class VehicleCoreBindingModule {
    @Binds
    @Singleton
    abstract fun bindVehicleCoreRepository(vehicleCoreRepositoryImpl: VehicleCoreRepositoryImpl): VehicleCoreRepository
}

@InstallIn(ApplicationComponent::class)
@Module
object VehicleCoreModule {

    @Provides
    @Singleton
    internal fun provideDatabase(@ApplicationContext context: Context): VehicleDatabase = Room
        .databaseBuilder(context, VehicleDatabase::class.java, "vehicleDatabase")
        .addMigrations(MIGRATION_6_7)
        //.fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    internal fun provideZoeDatabase(database: VehicleDatabase): ZoeDatabase = database

    @Provides
    @Singleton
    fun provideVehicleDao(database: VehicleDatabase) = database.vehicleDao()

    @Provides
    @Singleton
    internal fun provideVehicleCoreService(@Named(AuthenticationModule.AUTHENTICATED_HTTP_CLIENT) okHttpClient: OkHttpClient) =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(KAMEREON_BASE_URL)
            .build()
            .create(VehicleCoreService::class.java)

}