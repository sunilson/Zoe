package at.sunilson.networkingcore.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
object NetworkingCoreModule {

    const val UNAUTHENTICATED_HTTP_CLIENT = "unauthenticatedHttpClient"

    @Provides
    @ElementsIntoSet
    fun provideDefaultInterceptors(): Set<Interceptor> = emptySet()

    @Provides
    @Named(UNAUTHENTICATED_HTTP_CLIENT)
    fun provideOkHTTPClient(interceptors: @JvmSuppressWildcards Set<Interceptor>) = OkHttpClient
        .Builder()
        .apply {
            interceptors.forEach { addInterceptor(it) }
        }
        .build()
}
