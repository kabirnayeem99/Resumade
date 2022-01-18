package io.github.kabirnayeem99.resumade.data.dataSource

import io.github.kabirnayeem99.resumade.common.utilities.AppDispatchers
import io.github.kabirnayeem99.resumade.data.database.*
import io.github.kabirnayeem99.resumade.data.dtos.Education
import io.github.kabirnayeem99.resumade.data.dtos.Experience
import io.github.kabirnayeem99.resumade.data.dtos.Project
import io.github.kabirnayeem99.resumade.data.dtos.Resume
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ResumeLocalDataSource @Inject constructor(
    private val database: ResumeDatabase
) {
    fun getAllResume(): Flow<List<Resume>> {
        return database.resumeDao().getAllResume()
    }

    fun getResumeForId(resumeId: Long): Flow<Resume> {
        return database.resumeDao().getResumeForId(resumeId)
    }

    fun getSingleResumeForId(resumeId: Long) =
        database.resumeDao().getSingleResume(resumeId)

    suspend fun insertResume(resume: Resume): Long {
        return withContext(AppDispatchers.diskDispatcher) {
            database.resumeDao().insertResume(resume)
        }
    }

    suspend fun deleteResume(resume: Resume) {
        withContext(AppDispatchers.diskDispatcher) {
            database.resumeDao().deleteResume(resume)
        }
    }

    suspend fun deleteResumeForId(resumeId: Long) {
        withContext(AppDispatchers.diskDispatcher) {
            database.resumeDao().deleteResumeForId(resumeId)
        }
    }

    suspend fun updateResume(resume: Resume) {
        withContext(AppDispatchers.diskDispatcher) {
            database.resumeDao().updateResume(resume)
        }
    }

    fun getLastResumeId(): Flow<Long> {
        return database.resumeDao().getLastResumeId()
    }

    fun getAllEducationForResume(resumeId: Long): Flow<List<Education>> {
        return database.educationDAO().getEducationForResume(resumeId)
    }

    fun getAllEducationForResumeOnce(resumeId: Long): List<Education> {
        return database.educationDAO().getEducationForResumeOnce(resumeId)
    }

    suspend fun insertEducation(education: Education): Long {
        return withContext(AppDispatchers.diskDispatcher) {
            database.educationDAO().insertEducation(education)
        }
    }

    suspend fun deleteEducation(education: Education) {
        withContext(AppDispatchers.diskDispatcher) {
            database.educationDAO().deleteEducation(education)
        }
    }

    suspend fun updateEducation(education: Education) {
        withContext(AppDispatchers.diskDispatcher) {
            database.educationDAO().updateEducation(education)
        }
    }

    fun getAllExperienceForResume(resumeId: Long): Flow<List<Experience>> {
        return database.experienceDAO().getExperienceForResume(resumeId)
    }

    fun getAllExperienceForResumeOnce(resumeId: Long): List<Experience> {
        return database.experienceDAO().getExperienceForResumeOnce(resumeId)
    }

    suspend fun insertExperience(experience: Experience): Long {
        return withContext(AppDispatchers.diskDispatcher) {
            database.experienceDAO().insertExperience(experience)
        }
    }

    suspend fun deleteExperience(experience: Experience) {
        withContext(AppDispatchers.diskDispatcher) {
            database.experienceDAO().deleteExperience(experience)
        }
    }

    suspend fun updateExperience(experience: Experience) {
        withContext(AppDispatchers.diskDispatcher) {
            database.experienceDAO().updateExperience(experience)
        }
    }

    fun getAllProjectsForResume(resumeId: Long): Flow<List<Project>> {
        return database.projectsDAO().getProjectsForResume(resumeId)
    }

    fun getAllProjectsForResumeOnce(resumeId: Long): List<Project> {
        return database.projectsDAO().getProjectsForResumeOnce(resumeId)
    }

    suspend fun insertProject(project: Project): Long {
        return withContext(AppDispatchers.diskDispatcher) {
            database.projectsDAO().insertProject(project)
        }
    }

    suspend fun deleteProject(project: Project) {
        withContext(AppDispatchers.diskDispatcher) {
            database.projectsDAO().deleteProject(project)
        }
    }

    suspend fun updateProject(project: Project) =
        withContext(AppDispatchers.diskDispatcher) {
            database.projectsDAO().updateProject(project)
        }
}