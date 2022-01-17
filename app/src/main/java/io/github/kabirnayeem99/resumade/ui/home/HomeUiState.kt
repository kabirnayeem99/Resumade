package io.github.kabirnayeem99.resumade.ui.home

import io.github.kabirnayeem99.resumade.domain.entity.ResumeOverview

data class HomeUiState(
    val isLoading: Boolean = false,
    val resumeList: List<ResumeOverview> = emptyList(),
    val message: String = "",
    val selectedResume: ResumeOverview? = null
)
