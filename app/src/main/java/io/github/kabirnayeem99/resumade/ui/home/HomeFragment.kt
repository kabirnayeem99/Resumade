package io.github.kabirnayeem99.resumade.ui.home

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
        lifecycleScope.launch {
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
        setHasOptionsMenu(true)
        navController = findNavController()
        binding?.apply {

            setupRecyclerView()
            fabAddResume.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToCreateResumeFragment(-1L)
                navController.navigate(action)
            }
        }
        setUpActionBar()
    }

    private fun setUpActionBar() {
        binding?.apply {
            val toolbar = materialSearchBar.getToolbar()
            materialSearchBar.apply {
                navigationIconCompat = R.drawable.ic_fluent_info_24_selector
                setHint(getString(R.string.search_for_resume))
                setOnClickListener {
                    materialSearchBar.requestFocus()
                }
                setNavigationOnClickListener {
                    materialSearchBar.requestFocus()
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main_activity, menu)
        val span = SpannableString("About")
        span.setSpan(ForegroundColorSpan(Color.BLACK), 0, span.length, 0)
        menu.getItem(0)?.title = span
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about -> {
                findNavController().navigate(R.id.action_homeFragment_to_aboutUsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

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

    private fun setupRecyclerView() {
        resumeAdapter = ResumeAdapter { resumeId: Long ->
            val action = HomeFragmentDirections.actionHomeFragmentToCreateResumeFragment(resumeId)
            navController.navigate(action)
        }
        val linearLayoutManager = LinearLayoutManager(requireContext())

        binding?.rvResumeList?.apply {
            adapter = resumeAdapter
            layoutManager = linearLayoutManager
        }
    }


}
