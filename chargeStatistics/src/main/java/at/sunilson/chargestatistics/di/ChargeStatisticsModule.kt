package at.sunilson.chargestatistics.di

import android.content.Context
import android.location.Geocoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*

@Module
@InstallIn(SingletonComponent::class)
object ChargeStatisticsModule {
    @Provides
    fun provideGeocoder(@ApplicationContext context: Context) =
        Geocoder(context, Locale.getDefault())
}
