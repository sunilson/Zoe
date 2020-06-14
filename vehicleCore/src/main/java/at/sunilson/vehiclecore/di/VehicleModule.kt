package at.sunilson.vehiclecore.di

import android.content.Context
import androidx.room.Room
import at.sunilson.vehiclecore.data.VehicleDao
import at.sunilson.vehiclecore.data.VehicleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@InstallIn(ActivityComponent::class)
@Module
object VehicleModule {

    @Provides
    internal fun provideVehicleDatabase(@ActivityContext context: Context): VehicleDao = Room
        .databaseBuilder(context, VehicleDatabase::class.java, "vehicleDatabase")
        .build()
        .vehicleDao()
}