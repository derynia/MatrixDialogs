package com.matrixdialogs.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.matrixdialogs.data.entity.Dialog

class DialogComparator: DiffUtil.ItemCallback<Dialog>() {

    override fun areItemsTheSame(oldItem: Dialog, newItem: Dialog) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Dialog, newItem: Dialog) = oldItem.item_id == newItem.item_id
}
