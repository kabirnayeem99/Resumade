package io.github.kabirnayeem99.resumade.data.dataSource

import io.github.kabirnayeem99.resumade.data.database.ResumeDatabase
import javax.inject.Inject

class ResumeDataSource @Inject constructor(
    private val database: ResumeDatabase
) {
}