package io.github.kabirnayeem99.resumade.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kabirnayeem99.resumade.data.dataSource.ResumeDataSource
import io.github.kabirnayeem99.resumade.data.database.ResumeDatabase

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    fun provideResumeDataSource(database: ResumeDatabase): ResumeDataSource {
        return ResumeDataSource(database)
    }
}