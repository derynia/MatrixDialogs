package com.matrixdialogs.home.adapter

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.matrixdialogs.data.entity.Dialog
import com.matrixdialogs.home.databinding.CardDialogRecyclerItemBinding
import com.matrixdialogs.playbackservice.service.isPlaying

class DialogAdapter(
    private val onButtonTextClick : (Dialog) -> Unit,
    private val onButtonTranslationClick : (Dialog) -> Unit,
    private val onNameClick : (Dialog) -> Unit,
    private val onPlayPauseClick : (Dialog) -> Unit
) : ListAdapter<Dialog, DialogViewHolder>(DialogComparator()) {

    fun setList(dialogs: List<Dialog>?) {
        dialogs?.let {
            submitList(dialogs)
        }
    }

    fun refreshIcons(currentlyPlaying : MediaMetadataCompat?, playbackState: PlaybackStateCompat?) {
        if (currentlyPlaying != null) {
            //Log.d("PlayingRepeat", currentlyPlaying.bundle.getLong(MEDIA_REPEATS_KEY).toString())
        }

        var currentId = -1
        if (currentlyPlaying != null) {
            when {
                playbackState?.isPlaying ?: false -> currentId = currentlyPlaying.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID).toInt()
                else -> Unit
            }
        }

        for (i in 0 until itemCount) {
            val item = getItem(i)
            val playing = item.item_id == currentId
            if (item.playing != playing) {
                item.playing = playing
                notifyItemChanged(i)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogViewHolder {
        val binding = CardDialogRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return DialogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        val dialog = getItem(position)
        holder.bind(dialog, onButtonTextClick, onButtonTranslationClick, onNameClick, onPlayPauseClick)
    }
}
