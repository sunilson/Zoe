package at.sunilson.zoe

import android.content.Context
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DebugApplicationModule {

    @Provides
    @Singleton
    internal fun provideFlipperInitializer(
        @ApplicationContext context: Context,
        networkFlipperPlugin: NetworkFlipperPlugin
    ): FlipperInitializer {
        return FlipperInitializerImpl(context, networkFlipperPlugin)
    }

    @Provides
    @Singleton
    fun provideFlipperPlugin() = NetworkFlipperPlugin()
}
