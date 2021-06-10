package at.sunilson.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.text.NumberFormat
import java.util.*
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NumberFormatModule {
    const val GERMAN_FORMAT = "german"

    @Provides
    @Named(GERMAN_FORMAT)
    fun provideGermanNumberFormatter(): NumberFormat =
        NumberFormat.getNumberInstance(Locale.GERMAN).apply { maximumFractionDigits = 2 }
}
