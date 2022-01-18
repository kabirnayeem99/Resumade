package io.github.kabirnayeem99.resumade.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.resumade.R
import io.github.kabirnayeem99.resumade.common.utilities.areAllItemsSaved
import io.github.kabirnayeem99.resumade.common.utilities.invisible
import io.github.kabirnayeem99.resumade.common.utilities.visible
import io.github.kabirnayeem99.resumade.data.dtos.Education
import io.github.kabirnayeem99.resumade.ui.createResume.CreateResumeFragment
import io.github.kabirnayeem99.resumade.ui.adapter.EducationAdapter
import io.github.kabirnayeem99.resumade.ui.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_education.*

@AndroidEntryPoint
class EducationFragment : Fragment() {

    private lateinit var educationAdapter: EducationAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val createResumeViewModel: CreateResumeViewModel by viewModels()
    private lateinit var educationRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        educationAdapter =
            EducationAdapter(
                { item: Education ->
                    // On Save Button Clicked
                    item.saved = true
                    createResumeViewModel.updateEducation(item)
                },
                { item: Education ->
                    // On Delete Button Clicked
                    createResumeViewModel.deleteEducation(item)
                    (activity as CreateResumeFragment).displaySnackbar("Education deleted.")
                },
                { item: Education ->
                    // On Edit Button Clicked
                    item.saved = false
                    createResumeViewModel.updateEducation(item)
                })

        linearLayoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_education, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        createResumeViewModel.educationList
            .observe(viewLifecycleOwner, Observer {
                educationAdapter.updateEducationList(it ?: emptyList())
                createResumeViewModel.educationDetailsSaved =
                    it == null || it.isEmpty() || it.areAllItemsSaved()
                toggleNoEducationLayout(it?.size ?: 0)
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        educationRecyclerView = view.findViewById(R.id.educationRecyclerView)

        educationRecyclerView.apply {
            adapter = educationAdapter
            layoutManager = linearLayoutManager
        }
    }

    private fun toggleNoEducationLayout(size: Int) {
        if (size > 0) {
            educationRecyclerView.visible()
            noEducationView.invisible()
        } else {
            educationRecyclerView.invisible()
            noEducationView.visible()
        }
    }
}