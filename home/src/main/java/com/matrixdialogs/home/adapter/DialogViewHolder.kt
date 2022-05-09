package com.matrixdialogs.home.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.matrixdialogs.core.playPauseIcon
import com.matrixdialogs.data.entity.Dialog
import com.matrixdialogs.home.databinding.CardDialogRecyclerItemBinding

class DialogViewHolder(private val binding: CardDialogRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private fun onTextClick(dialog: Dialog, onButtonClick: (Dialog) -> Unit) {
        onButtonClick(dialog)
    }

    private fun onNameClick(dialog: Dialog, onTextClick: (Dialog) -> Unit) {
        onTextClick(dialog)
    }

    private fun onPlayPauseClick(dialog: Dialog,onClick: (Dialog) -> Unit ) {
        onClick(dialog)
    }

    fun bind(
        dialog: Dialog,
        onButtonTextClick : (Dialog) -> Unit,
        onButtonTranslationClick : (Dialog) -> Unit,
        onButtonNameClick : (Dialog) -> Unit,
        onButtonPlayPauseClick : (Dialog) -> Unit
    )
    {
        with (binding) {
            textDialogName.text = dialog.name
            textDialogName.setOnClickListener { onNameClick(dialog, onButtonNameClick) }
            textCount.text = dialog.repeats.toString()
            buttonText.setOnClickListener { onTextClick(dialog, onButtonTextClick) }
            buttonTranslation.setOnClickListener { onTextClick(dialog, onButtonTranslationClick) }
            buttonPlay.setOnClickListener { onPlayPauseClick(dialog, onButtonPlayPauseClick) }
            buttonPlay.icon = ContextCompat.getDrawable(binding.root.context, playPauseIcon(dialog.playing))
        }
    }

    companion object {
        const val MAX_QTY = 99
        const val MIN_QTY = 1
    }
}