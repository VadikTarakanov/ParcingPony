package twine.data

import sqldelight.twin.data.SideSplitProgress
import twine.data.model.SideSplitProgressDto
import twine.data.model.TypeSplit

private const val TYPE_SPLIT_SIDE = "SIDE"

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

fun List<SideSplitProgress>.toDto(): List<SideSplitProgressDto> {
    return this.map { it.toDto() }
}

fun TypeSplit.toTypeSplitString() = when (this) {
    TypeSplit.SIDE -> TYPE_SPLIT_SIDE
}

fun String.toTypeSplitDto() = when (this) {
    TYPE_SPLIT_SIDE -> TypeSplit.SIDE
    else -> throw Exception("Invalid type of training")
}