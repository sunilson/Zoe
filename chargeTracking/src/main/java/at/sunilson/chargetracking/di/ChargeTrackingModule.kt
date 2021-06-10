package at.sunilson.chargetracking.di

import android.content.Context
import androidx.room.Room
import at.sunilson.chargetracking.data.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ChargeTrackingModule {
    @Provides
    @Singleton
    internal fun provideDatabase(@ApplicationContext context: Context): Database = Room
        .databaseBuilder(context, Database::class.java, "chargeTrackingDatabase")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    internal fun provideChargeTrackingDao(database: Database) = database.chargeTrackingDao()
}
