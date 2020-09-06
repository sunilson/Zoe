package at.sunilson.contracts.di

import android.content.Context
import androidx.room.Room
import at.sunilson.authentication.di.AuthenticationModule
import at.sunilson.contracts.data.ContractsDatabase
import at.sunilson.contracts.data.ContractsService
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
object ContractsModule {

    @Provides
    @Singleton
    internal fun provideDatabase(@ApplicationContext context: Context) = Room
        .databaseBuilder(context, ContractsDatabase::class.java, "contractsDatabase")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    internal fun provideContractsDao(database: ContractsDatabase) = database.contractsDao()

    @Provides
    @Singleton
    internal fun provideContractsService(@Named(AuthenticationModule.AUTHENTICATED_HTTP_CLIENT) okHttpClient: OkHttpClient) =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(Constants.KAMEREON_BASE_URL)
            .build()
            .create(ContractsService::class.java)
}