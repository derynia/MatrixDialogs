package com.matrixdialogs.langsetup.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.matrixdialogs.core.viewBinding
import com.matrixdialogs.langsetup.R
import com.matrixdialogs.langsetup.adapter.LangsAdapter
import com.matrixdialogs.langsetup.databinding.FragmentLangSelectBinding
import com.matrixdialogs.langsetup.viewmodel.LangSelectViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LangSelectFragment : Fragment(R.layout.fragment_lang_select) {
    private val binding: FragmentLangSelectBinding by viewBinding(FragmentLangSelectBinding::bind)
    private val langSelectViewModel: LangSelectViewModel by viewModels()
    private val adapter = LangsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.recyclerLangs.adapter = adapter
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                langSelectViewModel.dialogEvent.collect { event ->
                    adapter.setList(event)
                }
            }
        }

        binding.buttonLanguageAdd.setOnClickListener {
            Navigation.findNavController(this.requireView())
                .navigate(
                    R.id.action_langSelectFragment_to_addLangFragment,
                    bundleOf()
                )
        }
    }
}