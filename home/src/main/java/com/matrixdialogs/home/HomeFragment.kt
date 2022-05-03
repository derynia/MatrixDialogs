package com.matrixdialogs.home

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.matrixdialogs.core.viewBinding
import com.matrixdialogs.data.dataclass.LanguageSelected
import com.matrixdialogs.data.entity.Dialog
import com.matrixdialogs.home.adapter.DialogAdapter
import com.matrixdialogs.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

typealias coreString = com.matrixdialogs.core.R.string

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val homeViewModel: HomeViewModel by viewModels()
    private val adapter = DialogAdapter(
        { dialog -> openText(dialog) },
        { dialog -> openTranslation(dialog) },
        { dialog -> editDialog(dialog) },
        { dialog -> playPause(dialog) },
    )

    private fun navigateToTextFragment(text: String, header: String) {
        Navigation.findNavController(this.requireView())
            .navigate(
                R.id.action_homeFragment_to_showTextFragment,
                bundleOf(
                    getString(coreString.text_key) to text,
                    getString(coreString.header_key) to header
                )
            )
    }

    private fun openText(dialog: Dialog) {
        navigateToTextFragment(dialog.text, getString(coreString.dialog_text_name))
    }

    private fun openTranslation(dialog: Dialog) {
        navigateToTextFragment(dialog.translation, getString(coreString.dialog_translation_name))
    }

    private fun editDialog(dialog: Dialog) {
        navigateToAddEdit(dialog.item_id)
    }

    private fun playPause(dialog: Dialog) {
        homeViewModel.playOrToggle(dialog, true)
    }

    private fun navigateToAddEdit(dialogId: Int) {
        Navigation.findNavController(this.requireView())
            .navigate(
                R.id.action_homeFragment_to_addEditDialogFragment,
                bundleOf(
                    getString(coreString.lang_selected_key) to homeViewModel.currentLanguageSelected,
                    getString(coreString.dialog_id_key) to dialogId
                )
            )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setCurrentLanguage(result: LanguageSelected) {
        homeViewModel.currentLanguageSelected = result
        homeViewModel.refreshDialogs()
        binding.toolbar.subtitle = homeViewModel.currentLanguageSelected.toString()
    }

    private fun setUpViews() {
        binding.recyclerDialogs.adapter = adapter
        binding.toolbar.subtitle = homeViewModel.currentLanguageSelected.toString()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.dialogEvent.collect { event ->
                    adapter.setList(event)
                    homeViewModel.setMediaSource(event)
                }
            }
        }

        homeViewModel.currentlyPlaying.observe(viewLifecycleOwner) {
            adapter.refreshIcons(homeViewModel.currentlyPlaying.value, homeViewModel.playbackState.value)
        }

        homeViewModel.playbackState.observe(viewLifecycleOwner) {
            adapter.refreshIcons(homeViewModel.currentlyPlaying.value, homeViewModel.playbackState.value)
        }

        findNavController()
            .currentBackStackEntry?.savedStateHandle?.getLiveData<LanguageSelected>(getString(coreString.lang_selected_key))?.observe(viewLifecycleOwner)
            {result ->
                setCurrentLanguage(result)
            }

        binding.buttonLanguageSelect.setOnClickListener {
            Navigation.findNavController(this.requireView())
                .navigate(R.id.action_homeFragment_to_langselect_graph)
        }

        binding.buttonHelp.setOnClickListener {
            Navigation.findNavController(this.requireView())
                .navigate(
                    R.id.action_homeFragment_to_helpFragment
                )
        }

        binding.buttonAddOne.setOnClickListener {
            navigateToAddEdit(-1)
        }

        binding.buttonAddMany.setOnClickListener {
        }

        homeViewModel.playbackState.observe(viewLifecycleOwner) {

        }
    }
}
