package dev.skyit.yournews.api.models.headlines


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsListResponse(
    @SerialName("articles")
    val articles: List<ArticleDTO>,
    @SerialName("status")
    val status: String,
    @SerialName("totalResults")
    val totalResults: Int
)