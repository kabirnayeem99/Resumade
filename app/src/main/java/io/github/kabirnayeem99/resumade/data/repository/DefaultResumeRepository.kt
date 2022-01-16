package io.github.kabirnayeem99.resumade.data.repository

import io.github.kabirnayeem99.resumade.data.dataSource.ResumeDataSource
import io.github.kabirnayeem99.resumade.data.database.Education
import io.github.kabirnayeem99.resumade.data.database.Experience
import io.github.kabirnayeem99.resumade.data.database.Project
import io.github.kabirnayeem99.resumade.data.database.Resume
import io.github.kabirnayeem99.resumade.domain.repository.ResumeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DefaultResumeRepository @Inject constructor(
    private val dataSource: ResumeDataSource
) : ResumeRepository {

    override fun getAllResume(): Flow<List<Resume>> {
        return dataSource.getAllResume()
    }

    override fun getResumeForId(resumeId: Long): Flow<Resume> {
        return dataSource.getResumeForId(resumeId)
    }

    override fun getSingleResumeForId(resumeId: Long) =
        dataSource.getSingleResumeForId(resumeId)

    override suspend fun insertResume(resume: Resume): Long {
        return dataSource.insertResume(resume)
    }

    override suspend fun deleteResume(resume: Resume) {
        dataSource.deleteResume(resume)
    }

    override suspend fun deleteResumeForId(resumeId: Long) {
        dataSource.deleteResumeForId(resumeId)
    }

    override suspend fun updateResume(resume: Resume) {
        dataSource.updateResume(resume)
    }

    override fun getLastResumeId() = dataSource.getLastResumeId()

    override fun getAllEducationForResume(resumeId: Long) =
        dataSource.getAllEducationForResume(resumeId)


    override fun getAllEducationForResumeOnce(resumeId: Long): List<Education> {
        return dataSource.getAllEducationForResumeOnce(resumeId)
    }

    override suspend fun insertEducation(education: Education): Long {
        return dataSource.insertEducation(education)
    }

    override suspend fun deleteEducation(education: Education) {
        dataSource.deleteEducation(education)
    }

    override suspend fun updateEducation(education: Education) =
        dataSource.updateEducation(education)

    override fun getAllExperienceForResume(resumeId: Long) =
        dataSource.getAllExperienceForResume(resumeId)

    override fun getAllExperienceForResumeOnce(resumeId: Long) =
        dataSource.getAllExperienceForResumeOnce(resumeId)

    override suspend fun insertExperience(experience: Experience) =
        dataSource.insertExperience(experience)

    override suspend fun deleteExperience(experience: Experience) =
        dataSource.deleteExperience(experience)

    override suspend fun updateExperience(experience: Experience) =
        dataSource.updateExperience(experience)

    override fun getAllProjectsForResume(resumeId: Long): Flow<List<Project>> =
        dataSource.getAllProjectsForResume(resumeId)

    override fun getAllProjectsForResumeOnce(resumeId: Long) =
        dataSource.getAllProjectsForResumeOnce(resumeId)

    override suspend fun insertProject(project: Project) = dataSource.insertProject(project)

    override suspend fun deleteProject(project: Project) = dataSource.deleteProject(project)

    override suspend fun updateProject(project: Project) =
        dataSource.updateProject(project)
}