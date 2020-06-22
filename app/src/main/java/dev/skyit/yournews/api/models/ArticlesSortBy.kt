package dev.skyit.yournews.api.models

import kotlinx.serialization.Serializable

@Serializable
enum class ArticlesSortBy {
    RELEVANCE,
    POPULARITY,
    PUBLISHED_AT;

    override fun toString(): String {
        return when (this) {
            RELEVANCE -> "relevance"
            POPULARITY -> "popularity"
            PUBLISHED_AT -> "publishedAt"
        }
    }
}

@Serializable
enum class CategoryFilter {
    BUSINESS,
    ENTERTAINMENT,
    GENERAL,
    HEALTH,
    SCIENCE,
    SPORTS,
    TECHNOLOGY;

    override fun toString(): String {
        return when(this) {
            BUSINESS -> "Business"
            ENTERTAINMENT -> "Entertainment"
            GENERAL -> "General"
            HEALTH -> "Health"
            SCIENCE -> "Science"
            SPORTS -> "Sports"
            TECHNOLOGY -> "Technology"

        }
    }

    fun toQueryParameter() : String {
        return this.toString().toLowerCase()
    }
}

fun CategoryFilter.toParam() : String = this.toString().toLowerCase()