package twine.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import sqldelight.sqldelight.twin.data.SplitDatabase
import twine.data.model.SideSplitProgressDto

class ResultsRepository(
    val database: SplitDatabase
) {

    private val queries = database.splitDatabaseQueries

    fun insertResult(slideSplitProgress: SideSplitProgressDto) {
        queries.insertItem(
            progress = slideSplitProgress.progress.toLong(),
            date_training = slideSplitProgress.dateTraining,
            type_training = slideSplitProgress.splitType.toTypeSplitString()
        )
    }

    fun getAllResults(): Flow<List<SideSplitProgressDto>> {
        return queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { listSideSplitProgress ->
                listSideSplitProgress.toDto()
            }
    }
    fun deleteResult(id: Long) {
        return queries.deleteItem(id)
    }
}