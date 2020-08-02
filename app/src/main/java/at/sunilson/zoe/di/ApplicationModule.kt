package at.sunilson.zoe.di

import android.content.Context
import androidx.work.WorkManager
import at.sunilson.navigation.ActivityNavigator
import at.sunilson.presentationcore.splash.SplashShownHandler
import at.sunilson.zoe.MainActivity
import at.sunilson.zoe.navigation.ActivityNavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ApplicationComponent::class)
@Module
object ApplicationModule {
    @Provides
    fun provideActivityNavigator(): ActivityNavigator = ActivityNavigatorImpl()

    @Provides
    fun provideWorkmanager(@ApplicationContext context: Context) = WorkManager.getInstance(context)
}