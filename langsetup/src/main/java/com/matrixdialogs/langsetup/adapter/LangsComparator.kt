package com.matrixdialogs.langsetup.adapter

import androidx.recyclerview.widget.DiffUtil
import com.matrixdialogs.data.dataclass.LanguageSelected

class LangsComparator: DiffUtil.ItemCallback<LanguageSelected>() {

    override fun areItemsTheSame(oldItem: LanguageSelected, newItem: LanguageSelected) = oldItem == newItem

    override fun areContentsTheSame(oldItem: LanguageSelected, newItem: LanguageSelected) =
        (oldItem.sourceLanguage == newItem.sourceLanguage && oldItem.destLanguage == newItem.destLanguage)
}
