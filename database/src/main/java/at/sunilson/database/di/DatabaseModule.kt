package at.sunilson.database.di

import android.content.Context
import androidx.room.Room
import at.sunilson.database.Database
import at.sunilson.database.VehicleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    internal fun provideDatabase(@ApplicationContext context: Context): Database = Room
        .databaseBuilder(context, Database::class.java, "vehicleDatabase")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    internal fun provideVehicleDao(database: Database): VehicleDao = database.vehicleDao()
}