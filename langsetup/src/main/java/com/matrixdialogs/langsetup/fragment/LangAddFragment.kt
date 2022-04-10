package com.matrixdialogs.langsetup.fragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.matrixdialogs.core.LANG_PAIR_SAVE_ERROR
import com.matrixdialogs.core.showError
import com.matrixdialogs.core.viewBinding
import com.matrixdialogs.data.entity.Language
import com.matrixdialogs.langsetup.R
import com.matrixdialogs.langsetup.databinding.FragmentAddLangBinding
import com.matrixdialogs.langsetup.viewmodel.LangAddViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddLangFragment : Fragment(R.layout.fragment_add_lang) {
    private val binding: FragmentAddLangBinding by viewBinding(FragmentAddLangBinding::bind)
    private val langAddViewModel: LangAddViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                langAddViewModel.languageEvent.collect { event ->
                    binding.spinnerSourceLang.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, event)
                    binding.spinnerDestLang.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, event)
                }
            }
        }

        binding.buttonSubmit.setOnClickListener {
            val res = langAddViewModel.validateAndAddPair(binding.spinnerSourceLang.selectedItem as Language, binding.spinnerDestLang.selectedItem as Language)
            when {
                res.isBlank() -> activity?.onBackPressed()
                else -> {
                    showError(activity, LANG_PAIR_SAVE_ERROR, res)
                }
            }
        }
    }
}