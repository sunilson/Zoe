package at.sunilson.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import java.text.NumberFormat
import java.util.*
import javax.inject.Named

@Module
@InstallIn(ApplicationComponent::class)
object CoreApplicationModule {
    @Provides
    //TODO Use encrypted shared preferences
    fun provideSharedPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences("ZOE_APP", Context.MODE_PRIVATE)
}