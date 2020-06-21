package at.sunilson.core.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object CoreApplicationModule {

    @Provides
    //TODO Use encrypted shared preferences
    fun provideSharedPreferences(application: Application) =
        application.getSharedPreferences("ZOE_APP", Context.MODE_PRIVATE)
}