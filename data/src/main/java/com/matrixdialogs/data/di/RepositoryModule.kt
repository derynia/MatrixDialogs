package com.matrixdialogs.data.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {
//    @ViewModelScoped
//    @Binds
//    fun bindDialogRepository(dialogRepository: DialogRepository) : DialogRepository
}