package dev.skyit.yournews.repository.datasource

import dev.skyit.yournews.api.models.headlines.ArticleDTO

interface INewsHeadlines {
    suspend fun getHeadlinesPaged(country: String = "us", pageNumber: Int, pageSize: Int) : List<ArticleDTO>
}