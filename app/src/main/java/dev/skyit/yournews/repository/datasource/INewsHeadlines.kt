package dev.skyit.yournews.repository.datasource

import dev.skyit.yournews.api.models.headlines.Article

interface INewsHeadlines {
    suspend fun getHeadlinesPaged(country: String = "us", pageNumber: Int, pageSize: Int) : List<Article>
}