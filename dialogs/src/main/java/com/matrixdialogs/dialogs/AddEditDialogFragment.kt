package com.matrixdialogs.dialogs

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.matrixdialogs.core.viewBinding
import com.matrixdialogs.dialogs.databinding.FragmentAddEditDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditDialogFragment : Fragment(R.layout.fragment_add_edit_dialog) {
    private val binding: FragmentAddEditDialogBinding by viewBinding(FragmentAddEditDialogBinding::bind)
    private val addEditViewModel: AddEditDialogViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addEditViewModel.languageSelectedEvent.collect { event ->
                    binding.spinnerLangPair.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, event)
                }
            }
        }
    }
}
