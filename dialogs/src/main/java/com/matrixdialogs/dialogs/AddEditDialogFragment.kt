package com.matrixdialogs.dialogs

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.matrixdialogs.data.entity.Dialog
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
    private lateinit var languageSelectedAdapter : ArrayAdapter<LanguageSelected>
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            binding.editFieldFileName.setText(result.data?.dataString)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentLanguageSelected = arguments?.getParcelable(getString(coreString.lang_selected_key))
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
        languageSelectedAdapter = ArrayAdapter<LanguageSelected>(requireContext(), android.R.layout.simple_spinner_item, mutableListOf())

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addEditViewModel.languageSelectedEvent.collect { event ->
                    languageSelectedAdapter.addAll(event)
                    binding.spinnerLangPair.adapter = languageSelectedAdapter
                    binding.spinnerLangPair.setSelection(languageSelectedAdapter.getPosition(currentLanguageSelected))
                }
            }
        }

        binding.spinnerLangPair.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View, selectedItemPosition: Int, selectedId: Long
            ) {
                currentLanguageSelected = languageSelectedAdapter.getItem(selectedItemPosition)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.buttonTranslation.setOnClickListener {
            when {
                !binding.editFieldText.text.isNullOrBlank() -> {
                    toGoogleTranslate()
                }
                else -> showError(activity, LANG_PAIR_SAVE_ERROR, getString(R.string.err_text_is_empty))
            }
        }

        binding.buttonSubmit.setOnClickListener {
            val dialog = Dialog(0,
                                binding.editFieldName.text.toString(),
                                binding.editFieldFileName.text.toString(),
                                currentLanguageSelected?.sourceLanguage?.code ?: "",
                                currentLanguageSelected?.sourceLanguage,
                                currentLanguageSelected?.destLanguage?.code ?: "",
                                currentLanguageSelected?.destLanguage,
                                binding.editFieldText.text.toString(),
                                binding.editFieldTrans.text.toString(),
                            0
                            )
            val res = addEditViewModel.validateAndAddDialog(dialog)
            when {
                res.isBlank() -> activity?.onBackPressed()
                else -> {
                    showError(activity, LANG_PAIR_SAVE_ERROR, res)
                }
            }
        }

        binding.editFieldFileName.setOnClickListener {
            var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.type = "*/*"
            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
            resultLauncher.launch(chooseFile)
        }
    }
}
