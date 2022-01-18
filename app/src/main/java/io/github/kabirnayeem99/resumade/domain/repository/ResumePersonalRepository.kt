package io.github.kabirnayeem99.resumade.domain.repository

import io.github.kabirnayeem99.resumade.common.utilities.Resource
import io.github.kabirnayeem99.resumade.domain.entity.ResumeFull
import kotlinx.coroutines.flow.Flow

interface ResumePersonalRepository {
    fun saveResume(resumeFull: ResumeFull): Flow<Resource<Long>>
    fun getResumeById(resumeId: Long): Flow<Resource<ResumeFull>>
}