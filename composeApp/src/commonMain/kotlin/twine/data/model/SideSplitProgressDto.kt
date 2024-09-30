package twine.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SideSplitProgressDto(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("progress")
    val progress: Int,
    @SerialName("date_training")
    val dateTraining: String,
    @SerialName("split_type")
    val splitType: TypeSplit
)