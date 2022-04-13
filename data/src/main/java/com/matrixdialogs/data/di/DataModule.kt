package com.matrixdialogs.data.di

import android.content.Context
import com.matrixdialogs.data.MatrixDB
import com.matrixdialogs.data.SharedPrefsRepository
import com.matrixdialogs.data.repository.DialogRepository
import com.matrixdialogs.data.repository.LanguageRepository
import com.matrixdialogs.data.repository.LanguageSelectedRepository
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

    @Provides
    @Singleton
    fun provideLanguageSelectedRepository(matrixDB: MatrixDB) =  LanguageSelectedRepository(matrixDB)

    @Provides
    @Singleton
    fun provideLanguageRepository(matrixDB: MatrixDB) =  LanguageRepository(matrixDB)

    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext context: Context) =  SharedPrefsRepository(context)
}