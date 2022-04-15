package com.matrixdialogs.dialogs

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matrixdialogs.core.di.ApplicationModule
import com.matrixdialogs.data.dataclass.LanguageSelected
import com.matrixdialogs.data.entity.Dialog
import com.matrixdialogs.data.repository.DialogRepository
import com.matrixdialogs.data.repository.LanguageSelectedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class AddEditDialogViewModel @Inject constructor(
    private val languageSelectedRepository: LanguageSelectedRepository,
    private val dialogRepository: DialogRepository,
    private val externalScope: CoroutineScope = GlobalScope,
    private val resourcesProvider: ApplicationModule.ResourcesProvider
): ViewModel() {
    private var isEditValue = false
    var dialog = Dialog()

    private val mutableDialogEvent = MutableStateFlow<Boolean>(false)
    val dialogEvent : StateFlow<Boolean> = mutableDialogEvent

    var currentLanguageSelected: LanguageSelected? = null
    val languageSelectedEvent: StateFlow<List<LanguageSelected>>
        get() = languageSelectedRepository.getLanguageSelectedList()
            .map { it }
            .stateIn(viewModelScope, SharingStarted.Lazily, mutableListOf())

    fun isEdit() = isEditValue

    fun initializeDialog(dialogId: Int) {
        isEditValue = (dialogId != -1)
        if (dialogId != -1) {
            viewModelScope.launch(Dispatchers.IO) {
                dialog = dialogRepository.getById(dialogId)
                mutableDialogEvent.emit(true)
            }
        }
    }

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

    fun validateAndSaveDialog(): String =
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
