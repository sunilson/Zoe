package at.arkulpa.notifications.di

import at.arkulpa.notifications.data.NotificationRepositoryImpl
import at.arkulpa.notifications.domain.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationBindModule {
    @Binds
    abstract fun bindRepository(notificationRepositoryImpl: NotificationRepositoryImpl): NotificationRepository
}
