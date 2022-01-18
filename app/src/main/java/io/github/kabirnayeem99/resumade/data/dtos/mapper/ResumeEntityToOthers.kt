package io.github.kabirnayeem99.resumade.data.dtos.mapper

import io.github.kabirnayeem99.resumade.data.dtos.Resume
import io.github.kabirnayeem99.resumade.domain.entity.ResumeFull
import io.github.kabirnayeem99.resumade.domain.entity.ResumeOverview

fun Resume.toResumeOverview(): ResumeOverview {
    return ResumeOverview(
        id = id,
        resumeLabel = resumeName,
        personName = name,
        personEmail = email,
        personNumber = phone,
    )
}

fun Resume.toResumeFull(): ResumeFull {
    return ResumeFull(
        id = id,
        resumeLabel = resumeName,
        personName = name,
        personEmail = email,
        personPhoneNumber = phone,
        currentCity = currentCity,
        description = description,
        hobbies = hobbies,
        skills = skills
    )
}