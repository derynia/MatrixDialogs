package com.matrixdialogs.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matrixdialogs.data.entity.Dialog
import com.matrixdialogs.data.repository.DialogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dialogRepository: DialogRepository
): ViewModel() {
    val dialogEvent : StateFlow<List<Dialog>>
        get() = dialogRepository.getDialogs()
            .map { it }
            .stateIn(viewModelScope, SharingStarted.Lazily, mutableListOf())
}