package io.github.kabirnayeem99.resumade.data.dataSource

import android.util.Log
import io.github.kabirnayeem99.resumade.common.utilities.DispatcherProvider
import io.github.kabirnayeem99.resumade.common.utilities.Resource
import io.github.kabirnayeem99.resumade.data.database.ResumeDao
import io.github.kabirnayeem99.resumade.data.dtos.Resume
import io.github.kabirnayeem99.resumade.data.dtos.mapper.toResume
import io.github.kabirnayeem99.resumade.data.dtos.mapper.toResumeFull
import io.github.kabirnayeem99.resumade.data.dtos.mapper.updateResume
import io.github.kabirnayeem99.resumade.domain.entity.ResumeFull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "ResumePersonalLocalData"

class ResumePersonalLocalDataSource @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val resumeDao: ResumeDao,
) {

    fun saveResume(resumeFull: ResumeFull): Flow<Resource<Long>> {
        return flow {
            try {
                if (resumeFull.id == -1L) {
                    val resume: Resume = resumeDao.getResumeForIdOnce(resumeId = resumeFull.id)
                    val id: Long = updateResume(resumeFull, resume)
                    if (id == -1L) emit(Resource.Error("Failed to update the resume."))
                    else Resource.Success(id)
                } else {
                    val id: Long = insertNewResume(resumeFull)
                    if (id == -1L) emit(Resource.Error("Failed to save the new resume."))
                    else Resource.Success(id)
                }
            } catch (e: Exception) {
                Log.e(TAG, "saveResume: ${e.localizedMessage}", e)
                emit(Resource.Error(e.localizedMessage ?: "Failed to save resume."))
            }
        }
    }

    private suspend fun updateResume(resumeFull: ResumeFull, resume: Resume): Long {
        return withContext(dispatcher.io) {
            try {
                val updatedResume = resumeFull.updateResume(resume)
                resumeDao.updateResume(updatedResume)
                updatedResume.id
            } catch (e: Exception) {
                Log.e(TAG, "updateResume: ${e.localizedMessage}", e)
                -1L
            }
        }
    }

    private suspend fun insertNewResume(resumeFull: ResumeFull): Long {
        return withContext(dispatcher.io) {
            try {
                val resume = resumeFull.toResume()
                resumeDao.insertResume(resume)
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "insertNewResume: could not insert new resume, ${e.localizedMessage} ",
                    e
                )
                -1L
            }
        }
    }

    fun getResumeById(resumeId: Long): Flow<Resource<ResumeFull>> {
        return flow {
            try {
                val resume = resumeDao.getResumeForIdOnce(resumeId)
                val resumeFull = resume.toResumeFull()
                emit(Resource.Success(resumeFull))
            } catch (e: Exception) {
                val resume = Resume.emptyResume()
                val resumeFull = resume.toResumeFull()
                emit(Resource.Success(resumeFull))
            }
        }
    }
}