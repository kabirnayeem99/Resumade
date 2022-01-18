package io.github.kabirnayeem99.resumade.data.dtos.mapper

import io.github.kabirnayeem99.resumade.data.dtos.Resume
import io.github.kabirnayeem99.resumade.domain.entity.ResumeFull

fun ResumeFull.toResume(): Resume {
    return Resume(
        resumeName = resumeLabel,
        name = personName,
        phone = personPhoneNumber,
        email = personEmail,
        currentCity = currentCity,
        description = description,
        skills = skills,
        hobbies = hobbies,
    )
}

fun ResumeFull.updateResume(resume: Resume): Resume {
    val resumeFull = this
    resume.apply {
        resumeName = resumeFull.resumeLabel
        name = resumeFull.personName
        phone = resumeFull.personPhoneNumber
        email = resumeFull.personEmail
        currentCity = resumeFull.currentCity
        description = resumeFull.description
        hobbies = resumeFull.hobbies
        skills = resumeFull.skills
    }
    return resume
}