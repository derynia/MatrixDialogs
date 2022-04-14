package com.matrixdialogs.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.matrixdialogs.core.viewBinding
import com.matrixdialogs.home.databinding.FragmentShowTextBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowTextFragment : Fragment(R.layout.fragment_show_text) {
    private val binding: FragmentShowTextBinding by viewBinding(FragmentShowTextBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        binding.toolbar.title = arguments?.getString(getString(coreString.header_key))
        binding.textDialogText.text = arguments?.getString(getString(coreString.text_key))
    }
}
