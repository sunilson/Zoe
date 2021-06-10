package at.sunilson.chargeSchedule.di

import android.content.Context
import androidx.room.Room
import at.sunilson.authentication.di.AuthenticationModule
import at.sunilson.chargeSchedule.data.ChargeScheduleDatabase
import at.sunilson.chargeSchedule.data.ChargeScheduleService
import at.sunilson.vehiclecore.data.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ChargeScheduleModule {

    @Provides
    @Singleton
    internal fun provideDatabase(@ApplicationContext context: Context) = Room
        .databaseBuilder(context, ChargeScheduleDatabase::class.java, "chargeScheduleDatabase")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    internal fun provideChargeScheduleService(
        @Named(AuthenticationModule.AUTHENTICATED_HTTP_CLIENT) okHttpClient: OkHttpClient
    ) = Retrofit
        .Builder()
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(Constants.KAMEREON_BASE_URL)
        .build()
        .create(ChargeScheduleService::class.java)
}
