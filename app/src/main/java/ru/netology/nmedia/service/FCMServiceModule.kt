package ru.netology.nmedia.service

import android.content.Context
import androidx.room.Room
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.netology.nmedia.db.AppDb
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class FCMServiceModule {

    @Provides
    fun provideFCMService(): FirebaseMessagingService {
        return FirebaseMessagingService()
    }

}