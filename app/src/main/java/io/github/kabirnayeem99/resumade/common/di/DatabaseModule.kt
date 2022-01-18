package io.github.kabirnayeem99.resumade.common.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.resumade.data.database.ResumeDatabase
import io.github.kabirnayeem99.resumade.data.dtos.Resume
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideExampleResume(): Resume {
        val resume = Resume(
            "Example Resume",
            "Naimul Kabir",
            "+801812312345",
            "kabirnayeem.99@gmail.com",
            "Dhaka",
            "The most average guy on this planet. Will do any task you throw at me, and I'll do it in an average manner.",
            "Business Management, Average-anything",
            "Guitar, Biking, Cooking"
        )
        resume.saved = true
        return resume
    }

    @Singleton
    @Provides
    fun provideYourDatabase(
        @ApplicationContext app: Context,
        resume: Resume
    ): ResumeDatabase = Room.databaseBuilder(
        app,
        ResumeDatabase::class.java,
        "resumes"
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideResumeDao(db: ResumeDatabase) = db.resumeDao()

    @Singleton
    @Provides
    fun provideEducationDao(db: ResumeDatabase) = db.educationDAO()

    @Singleton
    @Provides
    fun provideExperienceDao(db: ResumeDatabase) = db.experienceDAO()

    @Singleton
    @Provides
    fun provideProjectDao(db: ResumeDatabase) = db.projectsDAO()
}