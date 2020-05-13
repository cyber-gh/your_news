package dev.skyit.yournews.repository

import androidx.paging.DataSource
import dev.skyit.yournews.api.client.INewsAPIClient
import dev.skyit.yournews.api.models.headlines.Article
import dev.skyit.yournews.repository.datasource.NewsHeadlinesDataSource

interface INewsRepository {
    suspend fun getHeadlines(country: String) : List<Article>

    fun headlinesDataSource(country: String) : DataSource<Int, Article>
}

class NewsRepository(
    private val api: INewsAPIClient
) : INewsRepository {
    override suspend fun getHeadlines(country: String): List<Article> {
        //TODO add caching
        return api.getHeadlines(country)
    }

    override fun headlinesDataSource(country: String): DataSource<Int, Article> {
        return NewsHeadlinesDataSource(country, api)
    }

}