package com.matrixdialogs.dialogs

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
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

    fun getTranslateIntent(text: CharSequence, langSelected: LanguageSelected) : Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        //intent.`package` = "com.google.android.apps.translate"

        val uri: Uri = Uri.Builder()
            .scheme("http")
            .authority("translate.google.com")
            .path("/m/translate")
            .appendQueryParameter(
                "q",
                text.toString()
            )
            .appendQueryParameter("tl", langSelected.destLanguage?.code) // target language
            .appendQueryParameter("sl", langSelected.sourceLanguage?.code) // source language
            .build()
        intent.data = uri
        intent.component = ComponentName(
            "com.google.android.apps.translate",
            "com.google.android.apps.translate.TranslateActivity"
        )

        return intent
    }
}
