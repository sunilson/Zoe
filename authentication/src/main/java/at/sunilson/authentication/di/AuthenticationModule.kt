package at.sunilson.authentication.di

import at.sunilson.authentication.data.GigyaService
import at.sunilson.authentication.data.KamereonService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ActivityComponent::class)
object AuthenticationModule {
    @Provides
    fun provideGigyaService(okHttpClient: OkHttpClient) = Retrofit
        .Builder()
        .client(okHttpClient)
        .baseUrl("https://accounts.eu1.gigya.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(GigyaService::class.java)

    @Provides
    fun provideKamereonService(okHttpClient: OkHttpClient) = Retrofit
        .Builder()
        .client(okHttpClient)
        .baseUrl("https://api-wired-prod-1-euw1.wrd-aws.com/commerce/v1/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(KamereonService::class.java)
}