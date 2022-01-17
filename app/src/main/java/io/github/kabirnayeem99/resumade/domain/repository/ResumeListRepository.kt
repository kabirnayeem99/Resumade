package io.github.kabirnayeem99.resumade.domain.repository

import io.github.kabirnayeem99.resumade.common.utilities.Resource
import io.github.kabirnayeem99.resumade.domain.entity.ResumeOverview
import kotlinx.coroutines.flow.Flow

interface ResumeListRepository {
    fun getResumeList(): Flow<Resource<List<ResumeOverview>>>
    fun deleteResume(resumeOverview: ResumeOverview): Flow<String>
}
