package com.matrixdialogs.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matrixdialogs.data.SharedPrefsRepository
import com.matrixdialogs.data.dataclass.LanguageSelected
import com.matrixdialogs.data.entity.Dialog
import com.matrixdialogs.data.repository.DialogRepository
import com.matrixdialogs.data.repository.LanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dialogRepository: DialogRepository,
    private val languageRepository: LanguageRepository,
    private val sharedPrefsRepository: SharedPrefsRepository
): ViewModel() {
    private val emptyLanguage = languageRepository.getEmptyLanguage()
    var currentLanguageSelected = LanguageSelected(emptyLanguage, emptyLanguage)
//    private val languageSelectedEvent = MutableStateFlow<LanguageSelected>(LanguageSelected(emptyLanguage, emptyLanguage))
//    private val languageSelectedStateEvent : StateFlow<LanguageSelected> = languageSelectedEvent

    private val mutableDialogEvent = MutableStateFlow<List<Dialog>>(mutableListOf())
    val dialogEvent : StateFlow<List<Dialog>> = mutableDialogEvent
//    val dialogEvent : StateFlow<List<Dialog>>
//        get() = dialogRepository.getDialogsByPair(currentLanguageSelected)
//            .map { it }
//            .stateIn(viewModelScope, SharingStarted.Lazily, mutableListOf())

    init {
        val cLanguage = sharedPrefsRepository.getCurrentSelected()
        if (cLanguage != null) {
            currentLanguageSelected = cLanguage
        }
        refreshDialogs()
    }

    fun refreshDialogs() {
        if (currentLanguageSelected.sourceLanguage != null && emptyLanguage != currentLanguageSelected.sourceLanguage) {
            sharedPrefsRepository.saveCurrentSelected(currentLanguageSelected)
        }

        viewModelScope.launch {
            mutableDialogEvent.emitAll(dialogRepository.getDialogsByPair(currentLanguageSelected))
        }
    }
}