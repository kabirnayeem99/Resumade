package io.github.kabirnayeem99.resumade.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.resumade.data.database.Education
import io.github.kabirnayeem99.resumade.data.database.Experience
import io.github.kabirnayeem99.resumade.data.database.Project
import io.github.kabirnayeem99.resumade.data.database.Resume
import io.github.kabirnayeem99.resumade.domain.repository.ResumeRepository
import io.github.kabirnayeem99.resumade.ui.home.HomeUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val repository: ResumeRepository
) : ViewModel() {


    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()


    private var deleteResumeJob: Job? = null
    fun deleteResume(resume: Resume) {
        deleteResumeJob?.cancel()
        deleteResumeJob = viewModelScope.launch {
            repository.deleteResume(resume)
        }
    }

    private var fetchResumeJob: Job? = null
    fun getResumeList() {
        fetchResumeJob?.cancel()
        fetchResumeJob = viewModelScope.launch {
            repository.getAllResume().collect { resumeList ->
                if (resumeList.isNotEmpty()) _homeUiState.update {
                    it.copy(resumeList = resumeList)
                }
            }
        }
    }


    private var getResumeForIdJob: Job? = null
    fun getResumeForId(resumeId: Long) {
        getResumeForIdJob?.cancel()
        getResumeForIdJob = viewModelScope.launch {
            repository.getResumeForId(resumeId).collect { resume ->
                _homeUiState.update {
                    it.copy(selectedResume = resume)
                }
            }
        }
    }

    fun getEducationForResume(resumeId: Long): List<Education> =
        repository.getAllEducationForResumeOnce(resumeId)

    fun getExperienceForResume(resumeId: Long): List<Experience> =
        repository.getAllExperienceForResumeOnce(resumeId)

    fun getProjectForResume(resumeId: Long): List<Project> =
        repository.getAllProjectsForResumeOnce(resumeId)

}