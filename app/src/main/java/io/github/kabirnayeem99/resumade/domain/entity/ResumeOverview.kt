package io.github.kabirnayeem99.resumade.domain.entity

data class ResumeOverview(
    val id: Long = 0L,
    val resumeLabel: String = "",
    val personName: String = "",
    val personEmail: String = "",
    val personNumber: String = "",
)
