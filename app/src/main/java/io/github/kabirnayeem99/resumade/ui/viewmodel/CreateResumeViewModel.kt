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
import io.github.kabirnayeem99.resumade.ui.activities.CreateResumeFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateResumeViewModel
@Inject constructor(
    private val repository: ResumeRepository
) : ViewModel(),
    CoroutineScope {

    private val createResumeViewModelJob = Job()

    override val coroutineContext = Dispatchers.Main + createResumeViewModelJob

    private var resumeId: Long = 0L
    val resume = MutableLiveData<Resume>()
    val educationList = MutableLiveData<List<Education>>()
    val experienceList = MutableLiveData<List<Experience>>()
    val projectsList = MutableLiveData<List<Project>>()

    /*
    Initializing these values as true because
    the user might not want to add any of these
    details at all. These will be set to false
    upon the creation of an item in the respective
    categories, and then back to true when the item
    is saved. This also ensures that no empty item
    is saved too.
     */
    var personalDetailsSaved: Boolean = true
    var educationDetailsSaved: Boolean = true
    var experienceDetailsSaved: Boolean = true
    var projectDetailsSaved: Boolean = true


    init {
        viewModelScope.launch {
            resumeId = CreateResumeFragment.currentResumeId
            if (resumeId == -1L) {
                /*
                We can run this in a non blocking way
                because we don't care about the resumeId
                for a new resume as long as the user does
                not press the save button on the personal fragment,
                or the add button in any other fragments. Those events
                occur a long time after the viewmodel is created,
                so we can ignore the possibility of resumeId being -1L
                any time when it actually matters.
                 */
                resumeId =
                    repository.insertResume(Resume("My Resume", "", "", "", "", "", "", ""))
            }
            repository.getResumeForId(resumeId).collect {
                resume.postValue(it)
            }
            repository.getAllEducationForResume(resumeId).collect {
                educationList.postValue(it)
            }
            repository.getAllExperienceForResume(resumeId).collect {
                experienceList.postValue(it)
            }
            repository.getAllProjectsForResume(resumeId).collect {
                projectsList.postValue(it)
            }
        }
    }

    fun insertBlankEducation() = viewModelScope.launch {
        val education = Education("", "", "", "", resumeId)
        repository.insertEducation(education)
    }

    fun insertBlankExperience() = viewModelScope.launch {
        val experience = Experience("", "", "", resumeId)
        repository.insertExperience(experience)
    }

    fun insertBlankProject() = viewModelScope.launch {
        val project = Project("", "", "", "", resumeId)
        repository.insertProject(project)
    }

    fun updateResume(resume: Resume) = viewModelScope.launch {
        resume.id = resumeId
        repository.updateResume(resume)
    }

    fun updateEducation(education: Education) = viewModelScope.launch {
        repository.updateEducation(education)
    }

    fun updateExperience(experience: Experience) = viewModelScope.launch {
        repository.updateExperience(experience)
    }

    fun updateProject(project: Project) = viewModelScope.launch {
        repository.updateProject(project)
    }

    fun deleteEducation(education: Education) = viewModelScope.launch {
        repository.deleteEducation(education)
    }

    fun deleteExperience(experience: Experience) = viewModelScope.launch {
        repository.deleteExperience(experience)
    }

    fun deleteProject(project: Project) = viewModelScope.launch {
        repository.deleteProject(project)
    }

    fun deleteTempResume() = launch {
        repository.deleteResumeForId(resumeId)
    }

    override fun onCleared() {
        super.onCleared()
        createResumeViewModelJob.cancel()
    }
}