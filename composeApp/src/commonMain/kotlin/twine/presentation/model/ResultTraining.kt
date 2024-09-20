package twine.presentation.model

sealed class ResultTraining {

    data class CurrentResult(
        val percentResult: Float,
        val message: String
    ) : ResultTraining()

    data class Failed(
        val message: String
    ) : ResultTraining()
}