package twine.data

import sqldelight.twin.data.LongitundinalSplitProgressLeft
import sqldelight.twin.data.LongitundinalSplitProgressRight
import sqldelight.twin.data.SideSplitProgress
import sqldelight.twin.data.StaticLegsWorkOut
import twine.data.model.SideSplitProgressDto
import twine.data.model.TypeSplit

private const val TYPE_SPLIT_SIDE = "SIDE"
private const val TYPE_SPLIT_LEFT = "LEFT"
private const val TYPE_SPLIT_RIGHT = "RIGHT"
private const val TYPE_STATIC_LEG_WORKOUT = "LEG_WORKOUT"

fun SideSplitProgressDto.toDbModel(): SideSplitProgress {
    return SideSplitProgress(
        id = id ?: 0,
        progress = progress.toLong(),
        date_training = dateTraining,
        type_training = splitType.toTypeSplitString()
    )
}

fun SideSplitProgress.toDto(): SideSplitProgressDto {
    return SideSplitProgressDto(
        id = id,
        progress = progress?.toInt() ?: 0,
        dateTraining = date_training,
        splitType = type_training.toTypeSplitDto()
    )
}

fun List<LongitundinalSplitProgressLeft>.toLeftSplitToDto(): List<SideSplitProgressDto> {
    return this.map { it.toLeftSplitToDto() }
}

fun LongitundinalSplitProgressLeft.toLeftSplitToDto(): SideSplitProgressDto {
    return SideSplitProgressDto(
        id = id,
        progress = progress?.toInt() ?: 0,
        dateTraining = date_training,
        splitType = type_training.toTypeSplitDto()
    )
}

fun List<LongitundinalSplitProgressRight>.toRightSplitDto(): List<SideSplitProgressDto> {
    return this.map { it.toRightSplitDto() }
}

fun LongitundinalSplitProgressRight.toRightSplitDto(): SideSplitProgressDto {
    return SideSplitProgressDto(
        id = id,
        progress = progress?.toInt() ?: 0,
        dateTraining = date_training,
        splitType = type_training.toTypeSplitDto()
    )
}

fun List<StaticLegsWorkOut>.staticLegsWorkOutToDto(): List<SideSplitProgressDto> {
    return this.map { it.staticLegsWorkOutToDto() }
}

fun StaticLegsWorkOut.staticLegsWorkOutToDto(): SideSplitProgressDto {
    return SideSplitProgressDto(
        id = id,
        progress = progress?.toInt() ?: 0,
        dateTraining = date_training,
        splitType = type_training.toTypeSplitDto()
    )
}

fun List<SideSplitProgress>.toDto(): List<SideSplitProgressDto> {
    return this.map { it.toDto() }
}

fun TypeSplit.toTypeSplitString() = when (this) {
    TypeSplit.SIDE -> TYPE_SPLIT_SIDE
    TypeSplit.LEFT -> TYPE_SPLIT_LEFT
    TypeSplit.RIGHT -> TYPE_SPLIT_RIGHT
    TypeSplit.LEG_WORKOUT -> TYPE_STATIC_LEG_WORKOUT
}

fun String.toTypeSplitDto() = when (this) {
    TYPE_SPLIT_SIDE -> TypeSplit.SIDE
    TYPE_SPLIT_LEFT -> TypeSplit.LEFT
    TYPE_SPLIT_RIGHT -> TypeSplit.RIGHT
    TYPE_STATIC_LEG_WORKOUT -> TypeSplit.LEG_WORKOUT
    else -> throw Exception("Invalid type of training")
}