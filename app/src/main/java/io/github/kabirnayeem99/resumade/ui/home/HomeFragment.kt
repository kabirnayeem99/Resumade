package io.github.kabirnayeem99.resumade.ui.home

import android.os.Bundle
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.resumade.R
import io.github.kabirnayeem99.resumade.common.base.BaseFragment
import io.github.kabirnayeem99.resumade.common.ktx.showMessage
import io.github.kabirnayeem99.resumade.common.utilities.invisible
import io.github.kabirnayeem99.resumade.common.utilities.visible
import io.github.kabirnayeem99.resumade.databinding.FragmentHomeBinding
import io.github.kabirnayeem99.resumade.ui.adapter.ResumeAdapter
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var resumeAdapter: ResumeAdapter

    lateinit var navController: NavController

    override val layout: Int
        get() = R.layout.fragment_home

    override fun onCreated(savedInstanceState: Bundle?) {
        setUpViews()
        subscribeQuery()
    }

    private fun subscribeQuery() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.getResumeList()
                homeViewModel.homeUiState.collect {
                    handleUiState(it)
                }
            }
        }
    }

    private fun handleUiState(uiState: HomeUiState) {
        uiState.apply {
            if (isLoading) loadingIndicator.show() else loadingIndicator.dismiss()
            if (message.isNotEmpty()) showMessage(message)
            if (resumeList.isNotEmpty()) {
                resumeAdapter.submitList(resumeList)
                toggleNoResumesLayout(resumeList.size)
            }
        }
    }

    private fun setUpViews() {
        navController = findNavController()
        binding?.apply {
            setupRecyclerView(this)
            fabAddResume.setOnClickListener { navigateToCreateResumeScreen() }
        }
        setUpPopUpMenu()
    }

    private fun setUpPopUpMenu() {
        binding?.apply {
            val popup = PopupMenu(requireContext(), ivMenuButton)
            popup.menuInflater.inflate(R.menu.menu_home, popup.menu)
            popup.setOnMenuItemClickListener {
                 navigateToAboutScreen()
                true
            }
            ivMenuButton.setOnClickListener { popup.show() }
        }
    }

    private fun navigateToAboutScreen() {
        val action = HomeFragmentDirections.actionHomeFragmentToAboutUsFragment()
        navController.navigate(action)
    }

    private fun toggleNoResumesLayout(size: Int) {
        binding?.apply {
            if (size > 0) {
                rvResumeList.visible()
                layoutEmptyResumeList.root.invisible()
            } else {
                rvResumeList.visible()
                layoutEmptyResumeList.root.invisible()
            }
        }
    }

    private fun setupRecyclerView(binding: FragmentHomeBinding) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        resumeAdapter = ResumeAdapter { resumeId: Long ->
            navigateToCreateResumeScreen(resumeId)
        }
        binding.rvResumeList.apply {
            adapter = resumeAdapter
            layoutManager = linearLayoutManager
        }
    }

    private fun navigateToCreateResumeScreen(resumeId: Long = -1L) {
        val action = HomeFragmentDirections.actionHomeFragmentToCreateResumeFragment(resumeId)
        navController.navigate(action)
    }


}
