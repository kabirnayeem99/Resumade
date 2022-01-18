package io.github.kabirnayeem99.resumade.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.resumade.common.utilities.DispatcherProvider
import io.github.kabirnayeem99.resumade.data.dataSource.ResumeListLocalDataSource
import io.github.kabirnayeem99.resumade.data.dataSource.ResumeLocalDataSource
import io.github.kabirnayeem99.resumade.data.dataSource.ResumePersonalLocalDataSource
import io.github.kabirnayeem99.resumade.data.database.ResumeDao
import io.github.kabirnayeem99.resumade.data.database.ResumeDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideResumeDataSource(database: ResumeDatabase): ResumeLocalDataSource {
        return ResumeLocalDataSource(database)
    }

    @Provides
    @Singleton
    fun provideResumeListDataSource(
        database: ResumeDatabase,
        dispatcherProvider: DispatcherProvider
    ): ResumeListLocalDataSource {
        return ResumeListLocalDataSource(database, dispatcherProvider)
    }

    @Provides
    @Singleton
    fun provideResumePersonalDataSource(
        resumeDao: ResumeDao,
        dispatcherProvider: DispatcherProvider
    ): ResumePersonalLocalDataSource {
        return ResumePersonalLocalDataSource(dispatcherProvider, resumeDao)
    }
}