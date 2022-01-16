package io.github.kabirnayeem99.resumade.ui.home

import io.github.kabirnayeem99.resumade.data.database.Resume

data class HomeUiState(
    val isLoading: Boolean = false,
    val resumeList: List<Resume> = emptyList(),
    val message: String = "",
    val selectedResume: Resume = Resume.emptyResume()
)
