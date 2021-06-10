package at.sunilson.zoe

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReleaseApplicationModule {
    @Provides
    @Singleton
    internal fun provideFlipperInitializer(): FlipperInitializer {
        return FlipperInitializerImpl()
    }
}
