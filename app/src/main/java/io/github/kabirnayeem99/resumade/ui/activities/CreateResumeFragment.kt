package io.github.kabirnayeem99.resumade.ui.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.webkit.WebView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.resumade.R
import io.github.kabirnayeem99.resumade.common.base.BaseFragment
import io.github.kabirnayeem99.resumade.common.utilities.AppDispatchers
import io.github.kabirnayeem99.resumade.common.utilities.buildHtml
import io.github.kabirnayeem99.resumade.common.utilities.createPrintJob
import io.github.kabirnayeem99.resumade.common.utilities.hideKeyboard
import io.github.kabirnayeem99.resumade.databinding.FragmentCreateResumeBinding
import io.github.kabirnayeem99.resumade.ui.adapter.FragmentAdapter
import io.github.kabirnayeem99.resumade.ui.viewmodel.CreateResumeViewModel
import kotlinx.coroutines.*

@AndroidEntryPoint
class CreateResumeFragment : BaseFragment<FragmentCreateResumeBinding>(), CoroutineScope {

    private val createResumeActivityJob = Job()
    override val coroutineContext = Dispatchers.Main + createResumeActivityJob

    private val extraHtml: String = "html"
    private val createResumeViewModel: CreateResumeViewModel by viewModels()
    private lateinit var resumeFragmentAdapter: FragmentAdapter
    private lateinit var createResumeFab: FloatingActionButton
    private lateinit var viewPager: ViewPager
    private lateinit var webView: WebView
    private var addIcon: Drawable? = null
    private var doneIcon: Drawable? = null

    companion object {
        var currentResumeId: Long = -1L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

//    override fun onBackPressed() {
//        if (!createResumeViewModel.personalDetailsSaved || !createResumeViewModel.educationDetailsSaved || !createResumeViewModel.experienceDetailsSaved || !createResumeViewModel.projectDetailsSaved) {
//            MaterialAlertDialogBuilder(requireContext())
//                .setTitle("Unsaved Details")
//                .setMessage("Some details remain unsaved. Stay to view them.")
//                .setPositiveButton("Stay") { _, _ ->
//                    checkIfDetailsSaved()
//                }
//                .setNegativeButton("Delete") { _, _ ->
//                    createResumeViewModel.deleteTempResume()
//                    findNavController().navigateUp()
//                }
//                .create()
//                .show()
//        } else {
//            findNavController().navigateUp()
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_create_resume_activity, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.done -> run {
                (activity as HostActivity).hideKeyboard()
                if (checkIfDetailsSaved()) {
                    findNavController().navigateUp()
                }
                return true
            }
            R.id.print -> run {
                (activity as HostActivity).hideKeyboard()
                if (checkIfDetailsSaved()) {
                    launch(AppDispatchers.computationDispatcher) {
                        val html = buildHtml(
                            createResumeViewModel.resume.value!!,
                            createResumeViewModel.educationList.value!!,
                            createResumeViewModel.experienceList.value!!,
                            createResumeViewModel.projectsList.value!!
                        )
                        withContext(AppDispatchers.mainThreadDispatcher) {
                            webView = WebView(requireContext())
                            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
                            webView.createPrintJob(requireContext())
                        }
                    }
                }
                true
            }
            R.id.preview -> run {
                (activity as HostActivity).hideKeyboard()
                if (checkIfDetailsSaved()) {
                    launch(AppDispatchers.computationDispatcher) {
                        val html = buildHtml(
                            createResumeViewModel.resume.value!!,
                            createResumeViewModel.educationList.value!!,
                            createResumeViewModel.experienceList.value!!,
                            createResumeViewModel.projectsList.value!!
                        )
                        val intent = Intent(requireContext(), PreviewActivity::class.java)
                        intent.putExtra(extraHtml, html)
                        startActivity(intent)
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun adjustFabBehaviour(position: Int) {
        when (position) {
            0 -> fabBehaviourPersonalFragment()
            1 -> fabBehaviourEducationFragment()
            2 -> fabBehaviourExperienceFragment()
            3 -> fabBehaviourProjectFragment()
        }
    }

    private fun fabBehaviourPersonalFragment() {
        binding?.createResumeFab?.hide()
    }

    private fun fabBehaviourEducationFragment() {
        binding?.createResumeFab?.apply {
            hide()
            setImageDrawable(addIcon)
            show()
            setOnClickListener {
                createResumeViewModel.apply {
                    insertBlankEducation()
                    educationDetailsSaved = false
                }
            }
        }
    }

    private fun fabBehaviourExperienceFragment() {
        binding?.createResumeFab?.apply {
            hide()
            setImageDrawable(addIcon)
            show()
            setOnClickListener {
                createResumeViewModel.apply {
                    insertBlankExperience()
                    experienceDetailsSaved = false
                }
            }
        }
    }

    private fun fabBehaviourProjectFragment() {
        binding?.createResumeFab?.apply {
            hide()
            setImageDrawable(addIcon)
            show()
            setOnClickListener {
                createResumeViewModel.apply {
                    insertBlankProject()
                    projectDetailsSaved = false
                }
            }
        }
    }

    fun displaySnackbar(text: String) {
        binding?.apply {
            Snackbar.make(rootCoordinatorLayout, text, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun checkIfDetailsSaved(): Boolean {
        var isDetailsSaved = false
        binding?.apply {
            with(createResumeViewModel) {
                if (!personalDetailsSaved) {
                    viewPager.setCurrentItem(0, true)
                    Snackbar.make(
                        rootCoordinatorLayout,
                        "Personal details unsaved",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    isDetailsSaved = false
                }
                if (!educationDetailsSaved) {
                    viewPager.setCurrentItem(1, true)
                    Snackbar.make(
                        rootCoordinatorLayout,
                        "Education details unsaved",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    isDetailsSaved = false
                }
                if (!experienceDetailsSaved) {
                    viewPager.setCurrentItem(2, true)
                    Snackbar.make(
                        rootCoordinatorLayout,
                        "Experience details unsaved",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    isDetailsSaved = false
                }
                if (!projectDetailsSaved) {
                    viewPager.setCurrentItem(3, true)
                    Snackbar.make(
                        rootCoordinatorLayout,
                        "Project details unsaved",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    isDetailsSaved = false
                }
                isDetailsSaved = true
            }
        }
        return isDetailsSaved
    }

    override val layout: Int
        get() = R.layout.fragment_create_resume

    override fun onCreated(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        addIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_fluent_add_24_selector)
        doneIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_fluent_save_24_selector)

        if (arguments != null) {
            currentResumeId = arguments!!.getLong("resumeId")
        }

        viewPager = binding?.createResumeViewpager ?: ViewPager(context!!)

        resumeFragmentAdapter = FragmentAdapter(childFragmentManager)
        binding?.apply {
            viewPager.adapter = resumeFragmentAdapter
            viewPager.offscreenPageLimit = 4
            createResumeTabs.setupWithViewPager(createResumeViewpager)
            createResumeFab.hide()


            viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(p0: Int) {
                    // Do nothing
                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                    // Do nothing
                }

                override fun onPageSelected(position: Int) {
                    adjustFabBehaviour(position)
                }
            })
        }
    }
}

