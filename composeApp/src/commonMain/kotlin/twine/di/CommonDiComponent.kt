package twine.di

import app.cash.sqldelight.db.SqlDriver
import sqldelight.sqldelight.twin.data.SplitDatabase

import twine.data.ResultsRepository
import twine.data.TimerSettingsRepository
import twine.resourceprovider.ResourceProvider
import twine.resourceprovider.ResourceProviderImpl

class CommonDiComponent {

    companion object {
        private var timerSettingsRepository: TimerSettingsRepository? = null

        private var resourceProvider: ResourceProvider? = null

        private var resultsRepository: ResultsRepository? = null

        private var database: SplitDatabase? = null

        fun getTimerSettingsRepository(sqlDriver: SqlDriver): TimerSettingsRepository {
            return if (timerSettingsRepository == null) {
                timerSettingsRepository = TimerSettingsRepository(
                    database = getDataBase(sqlDriver)
                )
                timerSettingsRepository!!
            } else {
                timerSettingsRepository!!
            }
        }

        fun getResourceProvider(): ResourceProvider {
            return if (resourceProvider == null) {
                resourceProvider = ResourceProviderImpl()
                resourceProvider!!
            } else {
                resourceProvider!!
            }
        }

        fun getResultsRepository(sqlDriver: SqlDriver): ResultsRepository {
            return if (resultsRepository == null) {
                resultsRepository = ResultsRepository(database = getDataBase(sqlDriver))
                resultsRepository!!
            } else {
                resultsRepository!!
            }
        }

        private fun getDataBase(sqlDriver: SqlDriver): SplitDatabase {
            return if (database == null) {
                database = SplitDatabase(sqlDriver)
                database!!
            } else {
                database!!
            }
        }
    }
}