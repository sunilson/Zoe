package at.sunilson.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object CoreApplicationModule {
    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences("ZOE_APP", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideDatastore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.createDataStore(name = "ZOE_APP")
}