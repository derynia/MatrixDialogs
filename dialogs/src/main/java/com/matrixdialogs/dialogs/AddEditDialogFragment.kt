package com.matrixdialogs.dialogs

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.matrixdialogs.core.G_TRANSLATE_ERROR
import com.matrixdialogs.core.LANG_PAIR_SAVE_ERROR
import com.matrixdialogs.core.showError
import com.matrixdialogs.core.viewBinding
import com.matrixdialogs.data.dataclass.LanguageSelected
import com.matrixdialogs.dialogs.databinding.FragmentAddEditDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
typealias coreString = com.matrixdialogs.core.R.string

@AndroidEntryPoint
class AddEditDialogFragment : Fragment(R.layout.fragment_add_edit_dialog) {
    private val binding: FragmentAddEditDialogBinding by viewBinding(FragmentAddEditDialogBinding::bind)
    private val addEditViewModel: AddEditDialogViewModel by viewModels()
    private var currentLanguageSelected: LanguageSelected? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentLanguageSelected = arguments?.getParcelable(getString(coreString.lang_selected_key))
        Toast.makeText(context, currentLanguageSelected.toString(), Toast.LENGTH_LONG).show()
        setUpViews()
    }

    private fun toGoogleTranslate() {
        val intent = addEditViewModel.getTranslateIntent(binding.editFieldText.text.toString(), binding.spinnerLangPair.selectedItem as LanguageSelected)

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            showError(activity, G_TRANSLATE_ERROR, getString(R.string.no_google_translate))
        }
    }

    private fun setUpViews() {
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addEditViewModel.languageSelectedEvent.collect { event ->
                    val arAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, event)
                    binding.spinnerLangPair.adapter = arAdapter
                    binding.spinnerLangPair.setSelection(arAdapter.getPosition(currentLanguageSelected))
                }
            }
        }

        binding.buttonTranslation.setOnClickListener {
            when {
                !binding.editFieldText.text.isNullOrBlank() -> {
                    toGoogleTranslate()
                }
                else -> showError(activity, LANG_PAIR_SAVE_ERROR, getString(R.string.err_text_is_empty))
            }
        }
    }
}
