package com.matrixdialogs.langsetup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matrixdialogs.data.entity.Language
import com.matrixdialogs.data.entity.LanguagePairs
import com.matrixdialogs.data.repository.LanguageRepository
import com.matrixdialogs.data.repository.LanguageSelectedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LangAddViewModel @Inject constructor(
    private val languageRepository: LanguageRepository,
    private val languageSelectedRepository: LanguageSelectedRepository,
    private val externalScope: CoroutineScope = GlobalScope
): ViewModel() {
    val languageEvent : StateFlow<List<Language>>
        get() = languageRepository.getDialogs()
            .map { it }
            .stateIn(viewModelScope, SharingStarted.Lazily, mutableListOf())

    fun validateAndAddPair(sourceLang: Language?, destLang : Language?): String =
        when {
            sourceLang == destLang -> "Audio language must not be equal to translation language"
            sourceLang == null -> "Select Audio language"
            destLang == null -> "Select Translation language"
            else -> {
                externalScope.launch {
                    languageSelectedRepository.insert(
                        LanguagePairs(
                            sourceLang.item_id,
                            destLang.item_id
                        )
                    )
                }

                ""
            }
        }
 }