package io.github.kabirnayeem99.resumade.domain.entity

data class ResumeFull(
    var id: Long = -1L,
    var isSaved: Boolean = false,
    var resumeLabel: String = "",
    var personName: String = "",
    var personEmail: String = "",
    var personPhoneNumber: String = "",
    var currentCity: String = "",
    var description: String = "",
    var skills: String = "",
    var hobbies: String = "",
)