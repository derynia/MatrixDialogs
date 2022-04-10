package com.matrixdialogs.langsetup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.matrixdialogs.data.dataclass.LanguageSelected
import com.matrixdialogs.langsetup.databinding.CardLanguageRecyclerItemBinding

class LangsAdapter : ListAdapter<LanguageSelected, LangsViewHolder>(LangsComparator()) {
    fun setList(langsSelected: List<LanguageSelected>?) {
        langsSelected?.let {
            submitList(langsSelected)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LangsViewHolder {
        val binding = CardLanguageRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return LangsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LangsViewHolder, position: Int) {
        val langSelected = getItem(position)
        with (holder.binding) {
            textSourceLanguage.text = langSelected.sourceLanguage.name
            textDestLanguage.text = langSelected.destLanguage.name
        }
    }
}