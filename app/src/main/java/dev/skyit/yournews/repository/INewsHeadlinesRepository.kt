package dev.skyit.yournews.repository

import androidx.paging.DataSource
import dev.skyit.yournews.api.INetworkManger
import dev.skyit.yournews.api.caching.ArticlesDatabase
import dev.skyit.yournews.api.client.INewsAPIClient
import dev.skyit.yournews.api.models.headlines.ArticleDTO
import dev.skyit.yournews.repository.converters.toArticle
import dev.skyit.yournews.repository.converters.toEntity
import dev.skyit.yournews.repository.searching.ISearchNews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface INewsHeadlinesRepository {
    fun headlinesDataSource(country: String) : DataSource.Factory<Int, ArticleDTO>

    fun loadNextPage(country: String, pageSize: Int)
    suspend fun resetArticles(forCountry: String) : Boolean

    val reconnectedToInternet: Flow<Unit>
}



@InternalCoroutinesApi
class NewsRepository(
    private val api: INewsAPIClient,
    private val db: ArticlesDatabase,
    private val networkManager: INetworkManger,
    private val searchRepo: ISearchNews
) : INewsHeadlinesRepository, ISearchNews by searchRepo  {

    private val scope = CoroutineScope(Dispatchers.IO)

    // the first one is irrelevant, after that we take only those who give true which means the network is available
    override val reconnectedToInternet: Flow<Unit> = networkManager.hasInternet.drop(1).filter {
        it
    }.map { Unit }


    private fun cache(forCountry: String, data: List<ArticleDTO>) {
        val transformed = data.map { it.toEntity(forCountry) }.toTypedArray()
        scope.launch {
            db.articlesDao().insertAll(*transformed)
        }
    }

    override fun headlinesDataSource(country: String): DataSource.Factory<Int, ArticleDTO> {
        return db.articlesDao().articlesDataSource(country).map {
                it.toArticle()
        }

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