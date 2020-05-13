package dev.skyit.yournews.api.models

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