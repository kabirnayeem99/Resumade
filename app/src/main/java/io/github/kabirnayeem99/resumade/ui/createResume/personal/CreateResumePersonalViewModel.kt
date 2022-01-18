package io.github.kabirnayeem99.resumade.ui.createResume.personal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kabirnayeem99.resumade.domain.entity.ResumeFull
import io.github.kabirnayeem99.resumade.domain.repository.ResumePersonalRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateResumePersonalViewModel @Inject constructor(
    private val resumePersonalRepository: ResumePersonalRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _createResumeUiState = MutableStateFlow(CreateResumePersonalUiState())
    val createResumeUiState = _createResumeUiState.asStateFlow()

    private var saveResumeJob: Job? = null
    fun saveResume(resumeFull: ResumeFull) {
        saveResumeJob?.cancel()
        saveResumeJob = viewModelScope.launch {
            _createResumeUiState.update { it.copy(isLoading = true) }
            resumeFull.id = _createResumeUiState.value.resume.id
            resumePersonalRepository.saveResume(resumeFull).collect { res ->
                _createResumeUiState.update {
                    it.copy(
                        isLoading = false,
                        message = res.message ?: "Saved successfully."
                    )
                }
            }
        }
    }

    private var fetchResumeJob: Job? = null
    fun getResume() {
        val resumeId: Long = savedStateHandle.get<Long>("resumeId") ?: -1L
        fetchResumeJob = viewModelScope.launch {
            resumePersonalRepository.getResumeById(resumeId).collect { res ->
                _createResumeUiState.update {
                    it.copy(
                        isLoading = false,
                        message = res.message ?: "",
                        resume = res.data ?: ResumeFull()
                    )
                }
            }
        }
    }

}