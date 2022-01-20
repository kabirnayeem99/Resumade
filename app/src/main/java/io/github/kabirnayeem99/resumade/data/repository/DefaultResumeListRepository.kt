package io.github.kabirnayeem99.resumade.data.repository

import io.github.kabirnayeem99.resumade.common.utilities.Resource
import io.github.kabirnayeem99.resumade.data.dataSource.ResumeListLocalDataSource
import io.github.kabirnayeem99.resumade.domain.entity.ResumeOverview
import io.github.kabirnayeem99.resumade.domain.repository.ResumeListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultResumeListRepository @Inject constructor(
    private val dataSource: ResumeListLocalDataSource
) : ResumeListRepository {

    override fun getResumeList(): Flow<Resource<List<ResumeOverview>>> {
        return dataSource.getResumeOverviewList()
    }

    override fun deleteResume(resumeId: Long): Flow<String> {
        return dataSource.deleteResume(resumeId)
    }
}
