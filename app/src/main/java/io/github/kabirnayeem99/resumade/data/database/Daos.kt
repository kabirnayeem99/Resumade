package io.github.kabirnayeem99.resumade.data.database

import androidx.room.*
import io.github.kabirnayeem99.resumade.data.dtos.Education
import io.github.kabirnayeem99.resumade.data.dtos.Experience
import io.github.kabirnayeem99.resumade.data.dtos.Project
import io.github.kabirnayeem99.resumade.data.dtos.Resume
import kotlinx.coroutines.flow.Flow

@Dao
interface ResumeDao {

    @Query("SELECT * FROM resumes")
    fun getAllResume(): Flow<List<Resume>>

    @Query("SELECT * FROM resumes WHERE id=:resumeId")
    fun getResumeForId(resumeId: Long): Flow<Resume>

    @Query("SELECT * FROM resumes WHERE id=:resumeId")
    fun getResumeForIdOnce(resumeId: Long): Resume

    @Query("SELECT MAX(id) FROM resumes")
    fun getLastResumeId(): Flow<Long>

    @Insert
    fun insertResume(resume: Resume): Long

    @Delete
    fun deleteResume(resume: Resume)

    @Query("DELETE FROM resumes WHERE id=:resumeId")
    fun deleteResumeForId(resumeId: Long)

    @Update
    fun updateResume(resume: Resume)

    @Query("SELECT * FROM resumes WHERE id=:resumeId")
    fun getSingleResume(resumeId: Long): Resume
}

@Dao
interface EducationDAO {

    @Query("SELECT * FROM education")
    fun getAllEducation(): MutableList<Education>

    @Query("SELECT * FROM education WHERE resumeId=:resumeId")
    fun getEducationForResume(resumeId: Long): Flow<List<Education>>

    @Query("SELECT * FROM education WHERE resumeId=:resumeId")
    fun getEducationForResumeOnce(resumeId: Long): List<Education>

    @Query("SELECT count(*) FROM education")
    fun getEducationId(): Long

    @Insert
    fun insertEducation(education: Education): Long

    @Delete
    fun deleteEducation(education: Education)

    @Update
    fun updateEducation(education: Education)
}

@Dao
interface ExperienceDAO {

    @Query("SELECT * FROM experience")
    fun getAllExperience(): Flow<List<Experience>>

    @Query("SELECT * FROM experience WHERE resumeId=:resumeId")
    fun getExperienceForResume(resumeId: Long): Flow<List<Experience>>

    @Query("SELECT * FROM experience WHERE resumeId=:resumeId")
    fun getExperienceForResumeOnce(resumeId: Long): List<Experience>

    @Query("SELECT count(*) FROM experience")
    fun getExperienceId(): Long

    @Insert
    fun insertExperience(experience: Experience): Long

    @Delete
    fun deleteExperience(experience: Experience)

    @Update
    fun updateExperience(experience: Experience)
}

@Dao
interface ProjectsDAO {

    @Query("SELECT * FROM projects")
    fun getAllProjects(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE resumeId=:resumeId")
    fun getProjectsForResume(resumeId: Long): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE resumeId=:resumeId")
    fun getProjectsForResumeOnce(resumeId: Long): List<Project>

    @Query("SELECT count(*) FROM projects")
    fun getProjectId(): Long

    @Insert
    fun insertProject(project: Project): Long

    @Update
    fun updateProject(project: Project)

    @Delete
    fun deleteProject(project: Project)
}
