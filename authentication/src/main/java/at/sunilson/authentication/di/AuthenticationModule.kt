package at.sunilson.authentication.di

import at.sunilson.authentication.data.GigyaService
import at.sunilson.authentication.data.KamereonService
import at.sunilson.authentication.presentation.AuthenticationActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import retrofit2.Retrofit

@Module
@InstallIn(ActivityComponent::class)
object AuthenticationModule {
    @Provides
    fun provideGigyaService() = Retrofit
        .Builder()
        .baseUrl("TODO")
        .build()
        .create(GigyaService::class.java)

    @Provides
    fun provideKamereonService() = Retrofit
        .Builder()
        .baseUrl("TODO")
        .build()
        .create(KamereonService::class.java)
}