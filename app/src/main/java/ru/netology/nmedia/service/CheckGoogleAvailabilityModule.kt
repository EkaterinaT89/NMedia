package ru.netology.nmedia.service

import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class CheckGoogleAvailabilityModule {

    @Singleton
    @Provides
    fun checkGoogleAvailabilityModule(): GoogleApiAvailability = GoogleApiAvailability.getInstance()
}