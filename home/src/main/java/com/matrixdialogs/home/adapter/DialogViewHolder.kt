package com.matrixdialogs.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.matrixdialogs.data.entity.Dialog
import com.matrixdialogs.home.databinding.CardDialogRecyclerItemBinding

class DialogViewHolder(val binding: CardDialogRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
    private fun incrementCounter(dialog: Dialog) {
        dialog.repeats++
        dialog.repeats.coerceAtMost(MAX_QTY)
        binding.textRepeats.text = dialog.repeats.toString()
    }

    private fun decrementCounter(dialog: Dialog) {
        dialog.repeats--
        dialog.repeats.coerceAtLeast(MIN_QTY)
        binding.textRepeats.text = dialog.repeats.toString()
    }

    private fun onTextClick(dialog: Dialog, onButtonClick: (Dialog) -> Unit) {
        onButtonClick(dialog)
    }

    private fun onNameClick(dialog: Dialog, onTextClick: (Dialog) -> Unit) {
        onTextClick(dialog)
    }

    fun bind(
        dialog: Dialog,
        onButtonTextClick : (Dialog) -> Unit,
        onButtonTranslationClick : (Dialog) -> Unit,
        onNameClick : (Dialog) -> Unit
    )
    {
        with (binding) {
            textDialogName.text = dialog.name
            textDialogName.setOnClickListener { onNameClick(dialog, onNameClick) }
            textCount.text = ""
            textRepeats.text = dialog.repeats.toString()
            buttonIncrement.setOnClickListener { incrementCounter(dialog) }
            buttonDecrement.setOnClickListener { decrementCounter(dialog) }
            buttonText.setOnClickListener { onTextClick(dialog, onButtonTextClick) }
            buttonTranslation.setOnClickListener { onTextClick(dialog, onButtonTranslationClick) }
        }
    }

    companion object {
        const val MAX_QTY = 99
        const val MIN_QTY = 1
    }
}