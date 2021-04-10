package at.sunilson.networkingcore.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.multibindings.ElementsIntoSet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Named

@InstallIn(ApplicationComponent::class)
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
