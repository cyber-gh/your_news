package dev.skyit.yournews.repository

import androidx.paging.DataSource
import com.soywiz.klock.DateTime
import dev.skyit.yournews.api.caching.AppDatabase
import dev.skyit.yournews.api.caching.ArticleEntity
import dev.skyit.yournews.api.client.INewsAPIClient
import dev.skyit.yournews.api.models.headlines.Article
import dev.skyit.yournews.repository.datasource.NewsHeadlinesDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface INewsRepository {
    fun headlinesDataSource(country: String) : DataSource<Int, Article>
}

class NewsRepository(
    private val api: INewsAPIClient,
    private val db: AppDatabase
) : INewsRepository {

    private val scope = CoroutineScope(Dispatchers.IO)


    private fun cache(forCountry: String, data: List<Article>) {
        val transformed = data.map { it.toEntity(forCountry) }.toTypedArray()
        scope.launch {
            db.articlesDao().insertAll(*transformed)
        }
    }

    override fun headlinesDataSource(country: String): DataSource<Int, Article> {
        return NewsHeadlinesDataSource(country, api, onNewDataLoaded = {
            cache(country, it)
        })
    }

    private fun Article.toEntity(country: String) : ArticleEntity {
        return ArticleEntity(
            country = country,
            author = author,
            publishedAt = DateTime.parse(publishedAt).local.unixMillisLong,
            content = content,
            description = description,
            source = source,
            title = title,
            url = url,
            urlToImage = urlToImage

        )
    }

}