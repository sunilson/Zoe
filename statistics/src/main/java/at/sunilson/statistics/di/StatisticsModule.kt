package at.sunilson.statistics.di

import at.sunilson.authentication.di.AuthenticationModule
import at.sunilson.statistics.data.StatisticsRepositoryImpl
import at.sunilson.statistics.data.VehicleStatisticsService
import at.sunilson.statistics.domain.StatisticsRepository
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
abstract class StatisticsBindingModule {
    @Binds
    @Singleton
    internal abstract fun bindStatisticsRepository(
        statisticsRepositoryImpl: StatisticsRepositoryImpl
    ): StatisticsRepository
}

@InstallIn(ApplicationComponent::class)
@Module
object StatisticsModule {

    @Provides
    @Singleton
    internal fun provideStatisticsService(
        @Named(AuthenticationModule.AUTHENTICATED_HTTP_CLIENT) okHttpClient: OkHttpClient
    ) = Retrofit
        .Builder()
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://api-wired-prod-1-euw1.wrd-aws.com/commerce/v1/")
        .build()
        .create(VehicleStatisticsService::class.java)
}
