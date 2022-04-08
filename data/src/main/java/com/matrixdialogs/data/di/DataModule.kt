package com.matrixdialogs.data.di

import android.content.Context
import com.matrixdialogs.data.MatrixDB
import com.matrixdialogs.data.repository.DialogRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun getDatabase(@ApplicationContext context: Context): MatrixDB {
        return MatrixDB.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideDialogRepository(matrixDB: MatrixDB) =  DialogRepository(matrixDB)
}