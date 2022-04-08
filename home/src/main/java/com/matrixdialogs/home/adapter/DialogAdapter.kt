package com.matrixdialogs.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.matrixdialogs.data.entity.Dialog
import com.matrixdialogs.home.databinding.CardDialogRecyclerItemBinding

class DialogAdapter : ListAdapter<Dialog, DialogViewHolder>(DialogComparator()) {
    fun setList(dialogs: List<Dialog>?) {
        dialogs?.let {
            submitList(dialogs)
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
        with (holder.binding) {
            textDialogName.text = dialog.name
            textCount.text = ""
            textRepeats.text = dialog.repeats.toString()
        }
    }
}