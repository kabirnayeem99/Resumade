package io.github.kabirnayeem99.resumade.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.resumade.common.utilities.DispatcherProvider
import io.github.kabirnayeem99.resumade.data.dataSource.ResumeListLocalDataSource
import io.github.kabirnayeem99.resumade.data.dataSource.ResumeLocalDataSource
import io.github.kabirnayeem99.resumade.data.database.ResumeDatabase

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    fun provideResumeDataSource(database: ResumeDatabase): ResumeLocalDataSource {
        return ResumeLocalDataSource(database)
    }

    @Provides
    fun provideResumeListDataSource(
        database: ResumeDatabase,
        dispatcherProvider: DispatcherProvider
    ): ResumeListLocalDataSource {
        return ResumeListLocalDataSource(database, dispatcherProvider)
    }
}