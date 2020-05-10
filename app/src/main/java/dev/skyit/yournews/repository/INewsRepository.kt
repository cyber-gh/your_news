package dev.skyit.yournews.repository

import dev.skyit.yournews.api.client.INewsAPIClient
import dev.skyit.yournews.api.models.Article

interface INewsRepository {
    suspend fun getHeadlines(country: String) : List<Article>
}

class NewsRepository(
    private val api: INewsAPIClient
) : INewsRepository {
    override suspend fun getHeadlines(country: String): List<Article> {
        //TODO add caching
        return api.getHeadlines(country)
    }

}