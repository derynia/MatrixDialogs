package com.matrixdialogs.data.di

import com.matrixdialogs.data.repository.DialogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {
//    @ViewModelScoped
//    @Binds
//    fun bindDialogRepository(dialogRepository: DialogRepository) : DialogRepository
}