package com.matrixdialogs.home

import android.media.MediaMetadata.METADATA_KEY_MEDIA_ID
import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matrixdialogs.core.MEDIA_ROOT_ID
import com.matrixdialogs.data.SharedPrefsRepository
import com.matrixdialogs.data.dataclass.LanguageSelected
import com.matrixdialogs.data.entity.Dialog
import com.matrixdialogs.data.repository.DialogRepository
import com.matrixdialogs.data.repository.LanguageRepository
import com.matrixdialogs.playbackservice.service.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dialogRepository: DialogRepository,
    private val languageRepository: LanguageRepository,
    private val sharedPrefsRepository: SharedPrefsRepository,
    private val playServiceConnection: PlayServiceConnection,
    private val mediaSource : MediaSource
): ViewModel() {
    private val emptyLanguage = languageRepository.getEmptyLanguage()
    var currentLanguageSelected = LanguageSelected(emptyLanguage, emptyLanguage)

    private val mutableDialogEvent = MutableStateFlow<List<Dialog>>(mutableListOf())
    val dialogEvent : StateFlow<List<Dialog>> = mutableDialogEvent

    val isConnected = playServiceConnection.isConnected
    val currentlyPlaying = playServiceConnection.currentlyPlaying
    val playbackState = playServiceConnection.playbackState
    private var repeats : Int = 0

    init {
        repeats = sharedPrefsRepository.getRepeats()
        playServiceConnection.subscribe(MEDIA_ROOT_ID, object : MediaBrowserCompat.SubscriptionCallback() {
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                super.onChildrenLoaded(parentId, children)
                val items = children.map {
                    Dialog(
                        it.mediaId?.toInt() ?: 0,
                        it.description.title.toString()
                    )
                }
            }
        })

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

        viewModelScope.launch(Dispatchers.IO) {
            mutableDialogEvent.emitAll(dialogRepository.getDialogsByPair(currentLanguageSelected))
        }
    }

    fun setMediaSource(dialogs: List<Dialog>) {
        dialogs.forEach { it.repeats = repeats }

        mediaSource.clearData()
        dialogs.forEach {
            mediaSource.dialogs.add(it)
        }
        mediaSource.fetchData()
    }

    fun skipToNextSong() = playServiceConnection.transportControls.skipToNext()

    fun skipToPrevious() = playServiceConnection.transportControls.skipToPrevious()

    fun playOrToggle(mediaItem: Dialog, toggle: Boolean = false) : Boolean {
        val isPrepared = playbackState.value?.isPrepared ?: false
        if (isPrepared && mediaItem.item_id.toString()
            == currentlyPlaying.value?.getString(METADATA_KEY_MEDIA_ID)) {
            playbackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> if (toggle) {
                        playServiceConnection.transportControls.pause()
                        return false
                    }
                    playbackState.isPlayEnabled -> {
                        playServiceConnection.transportControls.play()
                        return true
                    }
                    else -> return false
                }
            }
        } else {
            playServiceConnection.transportControls.playFromMediaId(mediaItem.item_id.toString(), null)
            return true
         }

        return false
    }

    override fun onCleared() {
        super.onCleared()
        playServiceConnection.unsubscribe(MEDIA_ROOT_ID, object : MediaBrowserCompat.SubscriptionCallback() {})
    }
}