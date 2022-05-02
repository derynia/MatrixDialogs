package com.matrixdialogs.dialogs

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.matrixdialogs.core.*
import com.matrixdialogs.data.dataclass.LanguageSelected
import com.matrixdialogs.dialogs.databinding.FragmentAddEditDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


typealias coreString = com.matrixdialogs.core.R.string

@AndroidEntryPoint
class AddEditDialogFragment : Fragment(R.layout.fragment_add_edit_dialog) {

    private var isBound = false
    private val binding: FragmentAddEditDialogBinding by viewBinding(FragmentAddEditDialogBinding::bind)
    private val addEditViewModel: AddEditDialogViewModel by viewModels()
    private lateinit var languageSelectedAdapter : ArrayAdapter<LanguageSelected>
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data ?: Uri.parse("")
            val res = FilePathResolver.getFilePath(requireContext(), uri)
            binding.editFieldFileName.setText(res)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addEditViewModel.currentLanguageSelected = arguments?.getParcelable(getString(coreString.lang_selected_key))
        setUpViews()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun toGoogleTranslate() {
        val intent = addEditViewModel.getTranslateIntent(binding.editFieldText.text.toString(), binding.spinnerLangPair.selectedItem as LanguageSelected)

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            showError(activity, G_TRANSLATE_ERROR, getString(R.string.no_google_translate))
        }
    }

    private fun bindFields() {
        with(addEditViewModel.dialog) {
            fileName = binding.editFieldFileName.text.toString()
            name = binding.editFieldName.text.toString()
            languageFromCode =
                addEditViewModel.currentLanguageSelected?.sourceLanguage?.code ?: ""
            languageToCode = addEditViewModel.currentLanguageSelected?.destLanguage?.code ?: ""
            languageFrom = addEditViewModel.currentLanguageSelected?.sourceLanguage
            languageTo = addEditViewModel.currentLanguageSelected?.sourceLanguage
            text = binding.editFieldText.text.toString()
            translation = binding.editFieldTrans.text.toString()
        }
    }

    private fun bindDataFromDialogObject() {
        val d = addEditViewModel.dialog
        if (addEditViewModel.isEdit() && !isBound) {
            with (binding) {
                Log.d("CDialog", d.fileName)
                editFieldFileName.setText(d.fileName)
                editFieldName.setText(d.name)
                editFieldText.setText(d.text)
                editFieldTrans.setText(d.translation)
            }
        }
        isBound = true
        Log.d("CDialog", d.fileName)
    }

    private fun getAdaptersData() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addEditViewModel.dialogEvent.collect {
                    if (it) {
                        bindDataFromDialogObject()
                    }
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addEditViewModel.languageSelectedEvent.collect { event ->
                    languageSelectedAdapter.addAll(event)
                    binding.spinnerLangPair.adapter = languageSelectedAdapter
                    binding.spinnerLangPair.setSelection(languageSelectedAdapter.getPosition(addEditViewModel.currentLanguageSelected))
                }
            }
        }
    }

    private fun setOnClickListeners() {
        binding.buttonTranslation.setOnClickListener {
            when {
                !binding.editFieldText.text.isNullOrBlank() -> {
                    toGoogleTranslate()
                }
                else -> showError(activity, LANG_PAIR_SAVE_ERROR, getString(R.string.err_text_is_empty))
            }
        }

        binding.buttonSubmit.setOnClickListener {
            bindFields()
            val res = addEditViewModel.validateAndSaveDialog()
            when {
                res.isBlank() -> {
                    isBound = false
                    activity?.onBackPressed()
                }
                else -> showError(activity, LANG_PAIR_SAVE_ERROR, res)
            }
        }

        binding.editFieldFileName.setOnClickListener {
            val chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.addCategory(Intent.CATEGORY_OPENABLE)
            chooseFile.type = "audio/*"
            Intent.createChooser(chooseFile, "Choose a file")
            chooseFile.setFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            )
            resultLauncher.launch(chooseFile)
        }
    }

    private fun setUpViews() {
        languageSelectedAdapter = ArrayAdapter<LanguageSelected>(requireContext(), android.R.layout.simple_spinner_item, mutableListOf())

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        val dialogId = arguments?.getInt(getString(coreString.dialog_id_key))
        addEditViewModel.initializeDialog(dialogId ?: -1)
        getAdaptersData()
        setOnClickListeners()
    }
}
