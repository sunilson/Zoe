package at.sunilson.chargetracking.di

import android.content.Context
import androidx.room.Room
import at.sunilson.chargetracking.data.Database
import at.sunilson.chargetracking.data.MIGRATION_1_2
import at.sunilson.chargetracking.data.MIGRATION_2_3
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object ChargeTrackingModule {
    @Provides
    @Singleton
    internal fun provideDatabase(@ApplicationContext context: Context): Database = Room
        .databaseBuilder(context, Database::class.java, "chargeTrackingDatabase")
        .addMigrations(MIGRATION_1_2)
        .addMigrations(MIGRATION_2_3)
        .build()

    @Provides
    @Singleton
    internal fun provideChargeTrackingDao(database: Database) = database.chargeTrackingDao()
}