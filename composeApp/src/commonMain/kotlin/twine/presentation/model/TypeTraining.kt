package twine.presentation.model

enum class TypeTraining(val id: Int) {
    SIDE_SPLIT(0),
    LONGITUDINAL_SPLIT_LEFT(1),
    LONGITUDINAL_SPLIT_RIGHT(2),
    STATIC_LEG_WORKOUT(3)
}

fun Int.getTrainingTypeById() = when (this) {
    0 -> TypeTraining.SIDE_SPLIT
    1 -> TypeTraining.LONGITUDINAL_SPLIT_LEFT
    2 -> TypeTraining.LONGITUDINAL_SPLIT_RIGHT
    3 -> TypeTraining.STATIC_LEG_WORKOUT
    else -> throw IllegalArgumentException("Can't be with this id!")
}

fun TypeTraining.getIdByTrainingType() = when (this) {
    TypeTraining.SIDE_SPLIT -> this.id
    TypeTraining.LONGITUDINAL_SPLIT_LEFT -> this.id
    TypeTraining.LONGITUDINAL_SPLIT_RIGHT -> this.id
    TypeTraining.STATIC_LEG_WORKOUT -> this.id
    else -> throw IllegalArgumentException("Can't be with this id!")
}

