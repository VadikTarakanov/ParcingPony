package twine.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import sqldelight.sqldelight.twin.data.SplitDatabase
import twine.data.model.SideSplitProgressDto
import twine.presentation.model.TypeTraining

class ResultsRepository(
    val database: SplitDatabase
) {

    private val queriesSideSplit = database.splitDatabaseQueries
    private val queriesLongitudinalLeft = database.splitLongitudinalLeftQueries
    private val queriesLongitudinalRight = database.splitLongitudinalRightQueries
    private val queriesStaticLegsWorkoutQueries = database.staticLegsWorkoutQueries

    fun insertResult(
        slideSplitProgress: SideSplitProgressDto,
        typeTraining: TypeTraining
    ) {
        try {
            when (typeTraining) {
                TypeTraining.SIDE_SPLIT -> insertSideSplitResult(slideSplitProgress)
                TypeTraining.LONGITUDINAL_SPLIT_LEFT -> insertLongitudinalLeftResult(slideSplitProgress)
                TypeTraining.LONGITUDINAL_SPLIT_RIGHT -> insertLongitudinalRightResult(slideSplitProgress)
                TypeTraining.STATIC_LEG_WORKOUT -> insertStaticLegsWorkoutResult(slideSplitProgress)
            }
        } catch (e: Exception) {
            println("Can't insert result ${e.message}")
            println()
        }

    }

    fun getAllResults(typeTraining: TypeTraining): Flow<List<SideSplitProgressDto>> {
        return when (typeTraining) {
            TypeTraining.SIDE_SPLIT -> getAllResultsSideSplit()
            TypeTraining.LONGITUDINAL_SPLIT_LEFT -> getAllResultsLongitudinalLeftResult()
            TypeTraining.LONGITUDINAL_SPLIT_RIGHT -> getAllResultsLongitudinalRightResult()
            TypeTraining.STATIC_LEG_WORKOUT -> getAllResultsStaticLegWorkoutResult()
        }.catch {
            println("Can't getAllResult from DB ${it.message}")
            emptyList<SideSplitProgressDto>()
        }
    }

    fun deleteResult(typeTraining: TypeTraining, id: Long) {
        when (typeTraining) {
            TypeTraining.SIDE_SPLIT -> queriesSideSplit.deleteItem(id)
            TypeTraining.LONGITUDINAL_SPLIT_LEFT -> queriesLongitudinalLeft.deleteItem(id)
            TypeTraining.LONGITUDINAL_SPLIT_RIGHT -> queriesLongitudinalRight.deleteItem(id)
            TypeTraining.STATIC_LEG_WORKOUT -> queriesStaticLegsWorkoutQueries.deleteItem(id)
        }
    }

    private fun insertSideSplitResult(
        slideSplitProgress: SideSplitProgressDto,
    ) {
        queriesSideSplit.insertItem(
            progress = slideSplitProgress.progress.toLong(),
            date_training = slideSplitProgress.dateTraining,
            type_training = slideSplitProgress.splitType.toTypeSplitString()
        )
    }

    private fun insertLongitudinalLeftResult(
        slideSplitProgress: SideSplitProgressDto,
    ) {
        queriesLongitudinalLeft.insertItem(
            progress = slideSplitProgress.progress.toLong(),
            date_training = slideSplitProgress.dateTraining,
            type_training = slideSplitProgress.splitType.toTypeSplitString()
        )
    }

    private fun insertLongitudinalRightResult(
        slideSplitProgress: SideSplitProgressDto,
    ) {
        queriesLongitudinalRight.insertItem(
            progress = slideSplitProgress.progress.toLong(),
            date_training = slideSplitProgress.dateTraining,
            type_training = slideSplitProgress.splitType.toTypeSplitString()
        )
    }

    private fun insertStaticLegsWorkoutResult(
        slideSplitProgress: SideSplitProgressDto,
    ) {
        queriesStaticLegsWorkoutQueries.insertItem(
            progress = slideSplitProgress.progress.toLong(),
            date_training = slideSplitProgress.dateTraining,
            type_training = slideSplitProgress.splitType.toTypeSplitString()
        )
    }

    private fun getAllResultsSideSplit(): Flow<List<SideSplitProgressDto>> {
        return queriesSideSplit.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .catch {
                println("get")
            }
            .map { listSideSplitProgress ->
                listSideSplitProgress.toDto()
            }
    }

    private fun getAllResultsLongitudinalLeftResult(): Flow<List<SideSplitProgressDto>> {
        return queriesLongitudinalLeft.selectAll().asFlow()
            .mapToList(Dispatchers.IO)
            .map { listLeftSplitProgress ->
                listLeftSplitProgress.toLeftSplitToDto()
            }
    }

    private fun getAllResultsLongitudinalRightResult(): Flow<List<SideSplitProgressDto>> {
        return queriesLongitudinalRight.selectAll().asFlow()
            .mapToList(Dispatchers.IO)
            .map { listRightSplitProgress ->
                listRightSplitProgress.toRightSplitDto()
            }
    }

    private fun getAllResultsStaticLegWorkoutResult(): Flow<List<SideSplitProgressDto>> {
        return queriesLongitudinalRight.selectAll().asFlow()
            .mapToList(Dispatchers.IO)
            .map { listStaticLegWorkoutProgress ->
                listStaticLegWorkoutProgress.toRightSplitDto()
            }
    }
}