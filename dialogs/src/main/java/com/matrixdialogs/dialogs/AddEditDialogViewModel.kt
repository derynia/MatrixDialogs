package com.matrixdialogs.dialogs

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
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

    fun getTranslateIntent(text: CharSequence, langSelected: LanguageSelected) : Intent? {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, text.toString())
        intent.putExtra("key_text_input", text.toString())
        intent.putExtra("key_text_output", "")
        intent.putExtra("key_language_from", langSelected.sourceLanguage?.code)
        intent.putExtra("key_language_to", langSelected.destLanguage?.code)
        intent.putExtra("key_suggest_translation", "")
        intent.putExtra("key_from_floating_window", false)
        intent.component = ComponentName(
            "com.google.android.apps.translate",
            "com.google.android.apps.translate.TranslateActivity"
        )

        return intent
    }
}
