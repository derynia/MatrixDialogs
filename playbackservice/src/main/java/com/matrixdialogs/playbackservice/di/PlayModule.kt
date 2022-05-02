package com.matrixdialogs.playbackservice.di

import android.content.Context
import com.matrixdialogs.playbackservice.service.MediaSource
import com.matrixdialogs.playbackservice.service.PlayServiceConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PlayModule {

    @Singleton
    @Provides
    fun providePlayServiceConnection(@ApplicationContext context: Context) = PlayServiceConnection(context)

    @Singleton
    @Provides
    fun provideMediaSources() = MediaSource()
}