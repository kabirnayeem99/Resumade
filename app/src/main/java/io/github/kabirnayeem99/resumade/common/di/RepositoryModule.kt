package io.github.kabirnayeem99.resumade.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.github.kabirnayeem99.resumade.data.dataSource.ResumeListLocalDataSource
import io.github.kabirnayeem99.resumade.data.dataSource.ResumeLocalDataSource
import io.github.kabirnayeem99.resumade.data.dataSource.ResumePersonalLocalDataSource
import io.github.kabirnayeem99.resumade.data.repository.DefaultResumeListRepository
import io.github.kabirnayeem99.resumade.data.repository.DefaultResumePersonalRepository
import io.github.kabirnayeem99.resumade.data.repository.DefaultResumeRepository
import io.github.kabirnayeem99.resumade.domain.repository.ResumeListRepository
import io.github.kabirnayeem99.resumade.domain.repository.ResumePersonalRepository
import io.github.kabirnayeem99.resumade.domain.repository.ResumeRepository

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    fun provideResumeRepository(dataSource: ResumeLocalDataSource): ResumeRepository {
        return DefaultResumeRepository(dataSource)
    }

    @Provides
    fun provideResumeListRepository(dataSource: ResumeListLocalDataSource): ResumeListRepository {
        return DefaultResumeListRepository(dataSource)
    }


    @Provides
    fun provideResumePersonalRepository(dataSource: ResumePersonalLocalDataSource): ResumePersonalRepository {
        return DefaultResumePersonalRepository(dataSource)
    }
}