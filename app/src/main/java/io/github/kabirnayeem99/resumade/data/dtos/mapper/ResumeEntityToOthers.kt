package io.github.kabirnayeem99.resumade.data.dtos.mapper

import io.github.kabirnayeem99.resumade.data.database.Resume
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