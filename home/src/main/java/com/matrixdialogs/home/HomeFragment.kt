package com.matrixdialogs.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.matrixdialogs.core.viewBinding
import com.matrixdialogs.data.dataclass.LanguageSelected
import com.matrixdialogs.home.adapter.DialogAdapter
import com.matrixdialogs.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val homeViewModel: HomeViewModel by viewModels()
    private val adapter = DialogAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        binding.recyclerDialogs.adapter = adapter

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.dialogEvent.collect { event ->
                    adapter.setList(event)
                }
            }
        }

        findNavController()
            .currentBackStackEntry?.savedStateHandle?.getLiveData<LanguageSelected>("lSelected")?.observe(viewLifecycleOwner)
            {result ->
                Toast.makeText(context, result.toString(), Toast.LENGTH_LONG).show()
                homeViewModel.currentLanguageSelected = result
                homeViewModel.refreshDialogs()
            }

        binding.buttonLanguageSelect.setOnClickListener {
            Navigation.findNavController(this.requireView())
                .navigate(R.id.action_homeFragment_to_langselect_graph)
        }

        binding.buttonAddOne.setOnClickListener {
            Navigation.findNavController(this.requireView())
                .navigate(R.id.action_homeFragment_to_addEditDialogFragment)
        }

        binding.buttonAddMany.setOnClickListener {

        }
    }
}
