package io.github.kabirnayeem99.resumade.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import io.github.kabirnayeem99.resumade.data.dataSource.ResumeDataSource
import io.github.kabirnayeem99.resumade.data.repository.DefaultResumeRepository
import io.github.kabirnayeem99.resumade.domain.repository.ResumeRepository

@Module
@InstallIn(ViewModelComponent::class)
object ResumeModule {
    @Provides
    fun provideResumeRepository(dataSource: ResumeDataSource): ResumeRepository {
        return DefaultResumeRepository(dataSource)
    }
}