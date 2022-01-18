package io.github.kabirnayeem99.resumade.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.kabirnayeem99.resumade.data.dtos.Education
import io.github.kabirnayeem99.resumade.data.dtos.Experience
import io.github.kabirnayeem99.resumade.data.dtos.Project
import io.github.kabirnayeem99.resumade.data.dtos.Resume

@Database(
    entities = [(Resume::class), (Education::class), (Experience::class), (Project::class)],
    version = 1
)
abstract class ResumeDatabase : RoomDatabase() {
    abstract fun resumeDao(): ResumeDao
    abstract fun educationDAO(): EducationDAO
    abstract fun experienceDAO(): ExperienceDAO
    abstract fun projectsDAO(): ProjectsDAO
}