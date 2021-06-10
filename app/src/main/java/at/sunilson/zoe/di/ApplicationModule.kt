package at.sunilson.zoe.di

import android.content.Context
import androidx.work.WorkManager
import at.sunilson.navigation.ActivityNavigator
import at.sunilson.zoe.navigation.ActivityNavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApplicationModule {
    @Provides
    @Singleton
    fun provideActivityNavigator(): ActivityNavigator = ActivityNavigatorImpl()

    @Provides
    @Singleton
    fun provideWorkmanager(@ApplicationContext context: Context) = WorkManager.getInstance(context)
}
