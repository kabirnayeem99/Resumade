package io.github.kabirnayeem99.resumade.ui.createResume.personal

import io.github.kabirnayeem99.resumade.domain.entity.ResumeFull

data class CreateResumePersonalUiState(
    val isLoading: Boolean = false,
    val message: String = "",
    val resume: ResumeFull = ResumeFull()
)