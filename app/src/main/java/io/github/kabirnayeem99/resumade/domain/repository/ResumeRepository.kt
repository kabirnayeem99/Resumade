package io.github.kabirnayeem99.resumade.domain.repository

import io.github.kabirnayeem99.resumade.data.database.Education
import io.github.kabirnayeem99.resumade.data.database.Experience
import io.github.kabirnayeem99.resumade.data.database.Project
import io.github.kabirnayeem99.resumade.data.database.Resume
import kotlinx.coroutines.flow.Flow

interface ResumeRepository {

    /*
    Any data repository should implement
    following methods.
     */

    // Tasks related to resume-list
    fun getAllResume(): Flow<List<Resume>>
    fun getResumeForId(resumeId: Long): Flow<Resume>
    fun getSingleResumeForId(resumeId: Long): Resume
    fun getLastResumeId(): Flow<Long>
    suspend fun insertResume(resume: Resume): Long
    suspend fun deleteResume(resume: Resume)
    suspend fun updateResume(resume: Resume)
    suspend fun deleteResumeForId(resumeId: Long)

    // Tasks related to education-list
    fun getAllEducationForResume(resumeId: Long): Flow<List<Education>>
    fun getAllEducationForResumeOnce(resumeId: Long): List<Education>
    suspend fun insertEducation(education: Education): Long
    suspend fun deleteEducation(education: Education)
    suspend fun updateEducation(education: Education)

    // Tasks related to experience-list
    fun getAllExperienceForResume(resumeId: Long): Flow<List<Experience>>
    fun getAllExperienceForResumeOnce(resumeId: Long): List<Experience>
    suspend fun insertExperience(experience: Experience): Long
    suspend fun deleteExperience(experience: Experience)
    suspend fun updateExperience(experience: Experience)

    // Tasks related to projects-list
    fun getAllProjectsForResume(resumeId: Long): Flow<List<Project>>
    fun getAllProjectsForResumeOnce(resumeId: Long): List<Project>
    suspend fun insertProject(project: Project): Long
    suspend fun deleteProject(project: Project)
    suspend fun updateProject(project: Project)
}