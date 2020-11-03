package at.sunilson.zoe

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ReleaseApplicationModule {
    @Provides
    @Singleton
    internal fun provideFlipperInitializer() : FlipperInitializer {
        return FlipperInitializerImpl()
    }

}