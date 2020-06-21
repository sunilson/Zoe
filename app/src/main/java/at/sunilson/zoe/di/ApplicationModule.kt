package at.sunilson.zoe.di

import at.sunilson.navigation.ActivityNavigator
import at.sunilson.zoe.navigation.ActivityNavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
object ApplicationModule {
    @Provides
    fun provideActivityNavigator(): ActivityNavigator = ActivityNavigatorImpl()
}