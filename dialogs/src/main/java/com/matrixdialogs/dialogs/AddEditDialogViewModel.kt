package com.matrixdialogs.dialogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matrixdialogs.data.dataclass.LanguageSelected
import com.matrixdialogs.data.repository.LanguageSelectedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AddEditDialogViewModel @Inject constructor(
    private val languageSelectedRepository: LanguageSelectedRepository
): ViewModel() {
    val languageSelectedEvent: StateFlow<List<LanguageSelected>>
        get() = languageSelectedRepository.getLanguageSelectedList()
            .map { it }
            .stateIn(viewModelScope, SharingStarted.Lazily, mutableListOf())
}
