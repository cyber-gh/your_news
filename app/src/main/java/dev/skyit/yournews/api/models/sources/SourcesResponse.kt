package dev.skyit.yournews.api.models.sources


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SourcesResponse(
    @SerialName("sources")
    val sources: List<SourceExtended>,
    @SerialName("status")
    val status: String
)