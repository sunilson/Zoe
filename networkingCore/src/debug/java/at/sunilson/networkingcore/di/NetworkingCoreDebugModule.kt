package at.sunilson.networkingcore.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.multibindings.IntoSet
import okhttp3.Interceptor

@InstallIn(ApplicationComponent::class)
@Module
object NetworkingCoreDebugModule {

    @Provides
    @IntoSet
    fun provideChuckerInterceptor(@ApplicationContext context: Context): Interceptor =
        ChuckerInterceptor(
            context,
            collector = ChuckerCollector(context, showNotification = true)
        )

    @Provides
    @IntoSet
    fun provideFlipperInterceptor(networkFlipperPlugin: NetworkFlipperPlugin): Interceptor =
        FlipperOkhttpInterceptor(networkFlipperPlugin)
}
