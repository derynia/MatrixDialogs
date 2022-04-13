package com.matrixdialogs.langsetup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matrixdialogs.core.di.ApplicationModule
import com.matrixdialogs.data.entity.Language
import com.matrixdialogs.data.entity.LanguagePairs
import com.matrixdialogs.data.repository.LanguageRepository
import com.matrixdialogs.data.repository.LanguageSelectedRepository
import com.matrixdialogs.langsetup.R
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
    private val externalScope: CoroutineScope = GlobalScope,
    private val resourcesProvider: ApplicationModule.ResourcesProvider
): ViewModel() {
    val languageEvent : StateFlow<List<Language>>
        get() = languageRepository.getLanguages()
            .map { it }
            .stateIn(viewModelScope, SharingStarted.Lazily, mutableListOf())

    fun validateAndAddPair(sourceLang: Language?, destLang : Language?): String =
        when {
            sourceLang == destLang -> resourcesProvider.getString(R.string.err_lang_must_not_equal)
            sourceLang == null -> resourcesProvider.getString(R.string.err_select_source_lang)
            destLang == null -> resourcesProvider.getString(R.string.err_select_dest_lang)
            else -> {
                externalScope.launch {
                    languageSelectedRepository.insert(
                        LanguagePairs(
                            sourceLang.code,
                            destLang.code
                        )
                    )
                }

                ""
            }
        }
 }