package io.github.kabirnayeem99.resumade.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.resumade.R
import io.github.kabirnayeem99.resumade.common.base.BaseFragment
import io.github.kabirnayeem99.resumade.common.ktx.showMessage
import io.github.kabirnayeem99.resumade.common.utilities.buildHtml
import io.github.kabirnayeem99.resumade.common.utilities.createPrintJob
import io.github.kabirnayeem99.resumade.common.utilities.invisible
import io.github.kabirnayeem99.resumade.common.utilities.visible
import io.github.kabirnayeem99.resumade.data.database.Education
import io.github.kabirnayeem99.resumade.data.database.Experience
import io.github.kabirnayeem99.resumade.data.database.Project
import io.github.kabirnayeem99.resumade.data.database.Resume
import io.github.kabirnayeem99.resumade.databinding.FragmentHomeBinding
import io.github.kabirnayeem99.resumade.ui.activities.CreateResumeActivity
import io.github.kabirnayeem99.resumade.ui.adapter.ResumeAdapter
import io.github.kabirnayeem99.resumade.ui.adapter.SwipeToDeleteCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {


    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var resumeAdapter: ResumeAdapter
    private val webView: WebView by lazy {
        WebView(requireContext())
    }

    companion object {
        const val EXTRA_RESUME_ID: String = "resumeId"
    }

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
                resumeAdapter.updateResumesList(resumeList)
                toggleNoResumesLayout(resumeList.size)
            }
        }
    }

    private fun setUpViews() {
        setHasOptionsMenu(true)
        binding.apply {
            resumesListRecyclerView
            setupRecyclerView()
            fabAddResume.setOnClickListener {
                val newResumeId: Long = -1
                val intent = Intent(requireContext(), CreateResumeActivity::class.java)
                intent.putExtra(EXTRA_RESUME_ID, newResumeId)
                startActivity(intent)
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
        binding.apply {
            if (size > 0) {
                resumesListRecyclerView.visible()
                layoutEmptyResumeList.root.visibility = View.INVISIBLE
            } else {
                resumesListRecyclerView.invisible()
                layoutEmptyResumeList.root.visibility = View.INVISIBLE
            }
        }
    }

    private fun setupRecyclerView() {
        resumeAdapter = ResumeAdapter { resumeId: Long ->
            val intent = Intent(requireContext(), CreateResumeActivity::class.java)
            intent.putExtra(EXTRA_RESUME_ID, resumeId)
            startActivity(intent)
        }
        val linearLayoutManager = LinearLayoutManager(requireContext())

        binding.resumesListRecyclerView.apply {
            adapter = resumeAdapter
            layoutManager = linearLayoutManager
        }
        val itemTouchHelper = ItemTouchHelper(object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(
                viewholder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                direction: Int
            ) {
                val position = viewholder.adapterPosition
                val id: Long = resumeAdapter.getResumeAtPosition(position).id
                if (direction == ItemTouchHelper.LEFT) {
                    MaterialAlertDialogBuilder(
                        requireContext()
                    )
                        .setMessage("Are you sure you want to delete this resume?")
                        .setPositiveButton("Yes") { _, _ ->
                            homeViewModel.deleteResume(resumeAdapter.getResumeAtPosition(position))
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            resumeAdapter.notifyItemChanged(position)
                            dialog.dismiss()
                        }
                        .create()
                        .show()
                } else {
                    lifecycleScope.launch {
                        lateinit var resume: Resume
                        lateinit var educationList: List<Education>
                        lateinit var experienceList: List<Experience>
                        lateinit var projectList: List<Project>
                        lateinit var html: String

                        withContext(Dispatchers.IO) {
//                            resume = homeViewModel.getResumeForId(id)
                            educationList = homeViewModel.getEducationForResume(id)
                            experienceList = homeViewModel.getExperienceForResume(id)
                            projectList = homeViewModel.getProjectForResume(id)
                        }

                        withContext(Dispatchers.Default) {
                            html = buildHtml(resume, educationList, experienceList, projectList)
                        }

                        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
                        webView.createPrintJob(requireContext())
                        resumeAdapter.notifyItemChanged(position)
                    }
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.resumesListRecyclerView)
    }


}
