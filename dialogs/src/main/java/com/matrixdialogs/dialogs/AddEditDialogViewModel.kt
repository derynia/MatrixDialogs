package com.matrixdialogs.dialogs

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matrixdialogs.core.di.ApplicationModule
import com.matrixdialogs.data.dataclass.LanguageSelected
import com.matrixdialogs.data.entity.Dialog
import com.matrixdialogs.data.entity.Language
import com.matrixdialogs.data.entity.LanguagePairs
import com.matrixdialogs.data.repository.DialogRepository
import com.matrixdialogs.data.repository.LanguageSelectedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditDialogViewModel @Inject constructor(
    private val languageSelectedRepository: LanguageSelectedRepository,
    private val dialogRepository: DialogRepository,
    private val externalScope: CoroutineScope = GlobalScope,
    private val resourcesProvider: ApplicationModule.ResourcesProvider
): ViewModel() {
    val languageSelectedEvent: StateFlow<List<LanguageSelected>>
        get() = languageSelectedRepository.getLanguageSelectedList()
            .map { it }
            .stateIn(viewModelScope, SharingStarted.Lazily, mutableListOf())

    fun getTranslateIntent(text: CharSequence, langSelected: LanguageSelected) : Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND

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

    fun validateAndAddDialog(dialog: Dialog): String =
        when {
            dialog.fileName.isBlank() -> resourcesProvider.getString(R.string.filename_required)
            dialog.name.isBlank() -> resourcesProvider.getString(R.string.name_required)
            dialog.languageFrom == null -> resourcesProvider.getString(R.string.source_language_required)
            dialog.languageTo == null -> resourcesProvider.getString(R.string.dest_language_required)
            else -> {
                externalScope.launch {
                    dialogRepository.insert(dialog)
                }

                ""
            }
        }
}
