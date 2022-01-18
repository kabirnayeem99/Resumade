package io.github.kabirnayeem99.resumade.data.repository

import io.github.kabirnayeem99.resumade.common.utilities.Resource
import io.github.kabirnayeem99.resumade.data.dataSource.ResumePersonalLocalDataSource
import io.github.kabirnayeem99.resumade.domain.entity.ResumeFull
import io.github.kabirnayeem99.resumade.domain.repository.ResumePersonalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultResumePersonalRepository @Inject constructor(
    private val dataSource: ResumePersonalLocalDataSource
) : ResumePersonalRepository {
    override fun saveResume(resumeFull: ResumeFull): Flow<Resource<Long>> {
        return dataSource.saveResume(resumeFull)
    }

    override fun getResumeById(resumeId: Long): Flow<Resource<ResumeFull>> {
        return dataSource.getResumeById(resumeId)
    }
}