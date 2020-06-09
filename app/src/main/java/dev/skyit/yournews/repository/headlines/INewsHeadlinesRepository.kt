package dev.skyit.yournews.repository.headlines

import androidx.paging.DataSource
import dev.skyit.yournews.api.INetworkManger
import dev.skyit.yournews.repository.caching.ArticleEntity
import dev.skyit.yournews.repository.caching.IArticlesDatabase
import dev.skyit.yournews.api.client.INewsAPIClient
import dev.skyit.yournews.api.models.headlines.ArticleDTO
import dev.skyit.yournews.repository.converters.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber

interface INewsHeadlinesRepository {
    fun headlinesDataSource(country: String) : DataSource.Factory<Int, ArticleEntity>

    fun loadNextPage(country: String, pageSize: Int)
    suspend fun resetArticles(forCountry: String) : Boolean


}

@InternalCoroutinesApi
class NewsRepository(
    private val api: INewsAPIClient,
    private val db: IArticlesDatabase,
    private val networkManager: INetworkManger
) : INewsHeadlinesRepository {

    private val scope = CoroutineScope(Dispatchers.IO)




    private fun cache(forCountry: String, data: List<ArticleDTO>) {
        val transformed = data.map { it.toEntity(forCountry) }.toTypedArray()
        scope.launch {
            db.articlesDao().insertAll(*transformed)
        }
    }

    override fun headlinesDataSource(country: String): DataSource.Factory<Int, ArticleEntity> {
        return db.articlesDao().articlesDataSource(country)

    }

    override fun loadNextPage(country: String, pageSize: Int) {
        scope.launch {
            val nr = db.articlesDao().articlesNr(country)
            val fullPages = nr / pageSize
            val isLastPageFull = nr % pageSize == 0
            val nxtPage = if (isLastPageFull) fullPages + 1 else fullPages

            kotlin.runCatching {
                api.getHeadlinesPaged(country, nxtPage, pageSize)
            }.onSuccess {
                cache(country, it)
            }.onFailure {
                Timber.e(it, "something went wrong")
            }

        }
    }

    @InternalCoroutinesApi
    override suspend fun resetArticles(forCountry: String) : Boolean {
        val isConnected = networkManager.isConnected
        return if (isConnected) {
            db.articlesDao().deleteByCountry(forCountry)
            true
        } else {
            false
        }
    }

}