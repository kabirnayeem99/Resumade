package io.github.kabirnayeem99.resumade.data.dataSource

import android.util.Log
import io.github.kabirnayeem99.resumade.common.utilities.DispatcherProvider
import io.github.kabirnayeem99.resumade.common.utilities.Resource
import io.github.kabirnayeem99.resumade.data.database.ResumeDatabase
import io.github.kabirnayeem99.resumade.data.dtos.mapper.toResumeOverview
import io.github.kabirnayeem99.resumade.domain.entity.ResumeOverview
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ResumeListLocalDataSour"

class ResumeListLocalDataSource @Inject constructor(
    private val database: ResumeDatabase,
    private val dispatcher: DispatcherProvider,
) {
    fun getResumeOverviewList(): Flow<Resource<List<ResumeOverview>>> {
        return database.resumeDao().getAllResume().map { resumeList ->
            try {
                val resumeOverviewList = resumeList.map { resume ->
                    resume.toResumeOverview()
                }
                Resource.Success(resumeOverviewList)
            } catch (e: Exception) {
                Log.e(TAG, "getResumeOverviewList: ${e.localizedMessage}", e)
                Resource.Error(e.localizedMessage ?: "Could not get the resumes.")
            }
        }
    }

    fun deleteResume(resumeId: Long): Flow<String> {
        return flow {
            try {
                val resumeOverview =
                    database.resumeDao().getResumeForIdOnce(resumeId).toResumeOverview()
                database.resumeDao().deleteResumeForId(resumeId)
                coroutineScope {
                    val deleteEducationJob = launch { deleteEducationsForResumeId(resumeId) }
                    val deleteProjectJob = launch { deleteProjectsForResumeId(resumeId) }
                    val deleteExperienceJob = launch { deleteExperienceForResumeId(resumeId) }
                    joinAll(deleteEducationJob, deleteProjectJob, deleteExperienceJob)
                }
                emit("Successfully deleted \'${resumeOverview.resumeLabel}\' ")
                emit(resumeId.toString())
            } catch (e: Exception) {
                Log.e(TAG, "deleteResume: ${e.localizedMessage}", e)
                emit(e.localizedMessage ?: "Failed to delete.")
            }
        }.flowOn(dispatcher.io)
    }

    private fun deleteEducationsForResumeId(resumeId: Long) {
        try {
            val educations = database.educationDAO().getEducationForResumeOnce(resumeId)
            educations.forEach {
                database.educationDAO().deleteEducation(it)
            }
        } catch (e: Exception) {

            Log.e(TAG, "deleteEducationsForResumeId: ${e.localizedMessage} ", e)
        }
    }

    private fun deleteProjectsForResumeId(resumeId: Long) {
        try {
            val projects = database.projectsDAO().getProjectsForResumeOnce(resumeId)
            projects.forEach {
                database.projectsDAO().deleteProject(it)
            }
        } catch (e: Exception) {
            Log.e(TAG, "deleteProjectsForResumeId: ${e.localizedMessage} ", e)
        }
    }

    private fun deleteExperienceForResumeId(resumeId: Long) {
        try {
            val experiences = database.experienceDAO().getExperienceForResumeOnce(resumeId)
            experiences.forEach {
                database.experienceDAO().deleteExperience(it)
            }
        } catch (e: Exception) {
            Log.e(TAG, "deleteExperienceForResumeId: ${e.localizedMessage} ", e)
        }
    }
}