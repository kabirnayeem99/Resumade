package io.github.kabirnayeem99.resumade.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.resumade.domain.repository.ResumeListRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(private val repository: ResumeListRepository) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()

    private var deleteResumeJob: Job? = null
    fun deleteResume(resumeId: Long) {
        deleteResumeJob?.cancel()
        deleteResumeJob = viewModelScope.launch {
            repository.deleteResume(resumeId).collect { message ->
                _homeUiState.update { it.copy(message = message) }
            }
        }
    }

    private var fetchResumeJob: Job? = null
    fun getResumeList() {
        fetchResumeJob?.cancel()
        fetchResumeJob = viewModelScope.launch {
            _homeUiState.update { it.copy(isLoading = true) }
            repository.getResumeList().collect { resource ->
                _homeUiState.update {
                    it.copy(
                        resumeList = resource.data ?: emptyList(),
                        message = resource.message ?: "",
                        isLoading = false,
                        selectedResume = null,
                    )
                }
            }
        }
    }


}