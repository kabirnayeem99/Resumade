package io.github.kabirnayeem99.resumade.data.repository

import androidx.lifecycle.LiveData
import io.github.kabirnayeem99.resumade.common.utilities.AppDispatchers
import io.github.kabirnayeem99.resumade.data.database.*
import io.github.kabirnayeem99.resumade.domain.repository.ResumeRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DefaultResumeRepository @Inject constructor(
    private val database: ResumeDatabase
) : ResumeRepository {

    override fun getAllResume(): LiveData<List<Resume>> {
        return database.resumeDao().getAllResume()
    }

    override fun getResumeForId(resumeId: Long): LiveData<Resume> {
        return database.resumeDao().getResumeForId(resumeId)
    }

    override fun getSingleResumeForId(resumeId: Long) =
        database.resumeDao().getSingleResume(resumeId)

    override suspend fun insertResume(resume: Resume): Long {
        return withContext(AppDispatchers.diskDispatcher) {
            database.resumeDao().insertResume(resume)
        }
    }

    override suspend fun deleteResume(resume: Resume) {
        withContext(AppDispatchers.diskDispatcher) {
            database.resumeDao().deleteResume(resume)
        }
    }

    override suspend fun deleteResumeForId(resumeId: Long) {
        withContext(AppDispatchers.diskDispatcher) {
            database.resumeDao().deleteResumeForId(resumeId)
        }
    }

    override suspend fun updateResume(resume: Resume) {
        withContext(AppDispatchers.diskDispatcher) {
            database.resumeDao().updateResume(resume)
        }
    }

    override fun getLastResumeId(): LiveData<Long> {
        return database.resumeDao().getLastResumeId()
    }

    override fun getAllEducationForResume(resumeId: Long): LiveData<List<Education>> {
        return database.educationDAO().getEducationForResume(resumeId)
    }

    override fun getAllEducationForResumeOnce(resumeId: Long): List<Education> {
        return database.educationDAO().getEducationForResumeOnce(resumeId)
    }

    override suspend fun insertEducation(education: Education): Long {
        return withContext(AppDispatchers.diskDispatcher) {
            database.educationDAO().insertEducation(education)
        }
    }

    override suspend fun deleteEducation(education: Education) {
        withContext(AppDispatchers.diskDispatcher) {
            database.educationDAO().deleteEducation(education)
        }
    }

    override suspend fun updateEducation(education: Education) {
        withContext(AppDispatchers.diskDispatcher) {
            database.educationDAO().updateEducation(education)
        }
    }

    override fun getAllExperienceForResume(resumeId: Long): LiveData<List<Experience>> {
        return database.experienceDAO().getExperienceForResume(resumeId)
    }

    override fun getAllExperienceForResumeOnce(resumeId: Long): List<Experience> {
        return database.experienceDAO().getExperienceForResumeOnce(resumeId)
    }

    override suspend fun insertExperience(experience: Experience): Long {
        return withContext(AppDispatchers.diskDispatcher) {
            database.experienceDAO().insertExperience(experience)
        }
    }

    override suspend fun deleteExperience(experience: Experience) {
        withContext(AppDispatchers.diskDispatcher) {
            database.experienceDAO().deleteExperience(experience)
        }
    }

    override suspend fun updateExperience(experience: Experience) {
        withContext(AppDispatchers.diskDispatcher) {
            database.experienceDAO().updateExperience(experience)
        }
    }

    override fun getAllProjectsForResume(resumeId: Long): LiveData<List<Project>> {
        return database.projectsDAO().getProjectsForResume(resumeId)
    }

    override fun getAllProjectsForResumeOnce(resumeId: Long): List<Project> {
        return database.projectsDAO().getProjectsForResumeOnce(resumeId)
    }

    override suspend fun insertProject(project: Project): Long {
        return withContext(AppDispatchers.diskDispatcher) {
            database.projectsDAO().insertProject(project)
        }
    }

    override suspend fun deleteProject(project: Project) {
        withContext(AppDispatchers.diskDispatcher) {
            database.projectsDAO().deleteProject(project)
        }
    }

    override suspend fun updateProject(project: Project) =
        withContext(AppDispatchers.diskDispatcher) {
            database.projectsDAO().updateProject(project)
        }
}