package com.matrixdialogs.playbackservice.di

import android.content.Context
import com.matrixdialogs.playbackservice.service.PlayServiceConnection
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
class PlayModule {

    @Singleton
    @Provides
    fun providePlayServiceConnection(@ApplicationContext context: Context) = PlayServiceConnection(context)
}