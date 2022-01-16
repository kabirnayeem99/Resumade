package io.github.kabirnayeem99.resumade.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.resumade.R
import io.github.kabirnayeem99.resumade.common.utilities.areAllItemsSaved
import io.github.kabirnayeem99.resumade.common.utilities.invisible
import io.github.kabirnayeem99.resumade.common.utilities.visible
import io.github.kabirnayeem99.resumade.data.database.Experience
import io.github.kabirnayeem99.resumade.ui.activities.CreateResumeActivity
import io.github.kabirnayeem99.resumade.ui.adapter.ExperienceAdapter
import io.github.kabirnayeem99.resumade.ui.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_experience.*

@AndroidEntryPoint
class ExperienceFragment : Fragment() {

    private lateinit var experienceAdapter: ExperienceAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val createResumeViewModel: CreateResumeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        experienceAdapter = ExperienceAdapter(
            { item: Experience ->
                // On Save Button Click
                item.saved = true
                createResumeViewModel.apply {
                    updateExperience(item)
                }
            },
            { item: Experience ->
                // On Delete Button Click
                createResumeViewModel.deleteExperience(item)
                (activity as CreateResumeActivity).displaySnackbar("Experience deleted.")
            },
            { item: Experience ->
                // On Edit Button Click
                item.saved = false
                createResumeViewModel.apply {
                    updateExperience(item)
                }
            })
        linearLayoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_experience, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        createResumeViewModel.experienceList
            .observe(viewLifecycleOwner, Observer {
                experienceAdapter.updateExperienceList(it ?: emptyList())
                createResumeViewModel.experienceDetailsSaved =
                    it == null || it.isEmpty() || it.areAllItemsSaved()
                toggleNoExperienceLayout(it?.size ?: 0)
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        experienceRecyclerView.apply {
            adapter = experienceAdapter
            layoutManager = linearLayoutManager
        }
    }

    private fun toggleNoExperienceLayout(size: Int) {
        if (size > 0) {
            experienceRecyclerView.visible()
            noExperienceView.invisible()
        } else {
            experienceRecyclerView.invisible()
            noExperienceView.visible()
        }
    }
}
