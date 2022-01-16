package io.github.kabirnayeem99.resumade.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.resumade.data.database.Education
import io.github.kabirnayeem99.resumade.data.database.Experience
import io.github.kabirnayeem99.resumade.data.database.Project
import io.github.kabirnayeem99.resumade.data.database.Resume
import io.github.kabirnayeem99.resumade.domain.repository.ResumeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val repository: ResumeRepository
) : ViewModel(),
    CoroutineScope {

    private val mainViewModelJob = Job()

    override val coroutineContext = Dispatchers.Main + mainViewModelJob


    /*
    Cached list of resume with a private setter
    so that it can only be read from other classes,
    but not modified.
     */
    val resumesList = MutableLiveData<List<Resume>>()

    init {

        viewModelScope.launch {
            repository.getAllResume().collect {
                resumesList.postValue(it)
            }
        }
    }

    /*
    The main view model extends AndroidViewModel,
    so it gets an instance of application in its constructor.
    We use this to initialize the repository,
    which in turn initializes the database.
     */

    fun deleteResume(resume: Resume) = launch {
        repository.deleteResume(resume)
    }

    fun getResumeForId(resumeId: Long): Resume = repository.getSingleResumeForId(resumeId)

    fun getEducationForResume(resumeId: Long): List<Education> =
        repository.getAllEducationForResumeOnce(resumeId)

    fun getExperienceForResume(resumeId: Long): List<Experience> =
        repository.getAllExperienceForResumeOnce(resumeId)

    fun getProjectForResume(resumeId: Long): List<Project> =
        repository.getAllProjectsForResumeOnce(resumeId)

}