package dev.skyit.yournews.repository.headlines

import androidx.paging.DataSource
import dev.skyit.yournews.api.INetworkManger
import dev.skyit.yournews.repository.database.ArticleEntity
import dev.skyit.yournews.api.client.INewsAPIClient
import dev.skyit.yournews.api.models.CategoryFilter
import dev.skyit.yournews.api.models.CountryFilter
import dev.skyit.yournews.api.models.headlines.ArticleDTO
import dev.skyit.yournews.repository.converters.toEntity
import dev.skyit.yournews.repository.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

interface INewsHeadlinesRepository {
    fun headlinesDataSource(country: String) : DataSource.Factory<Int, ArticleEntity>

    fun loadNextPage(country: String, pageSize: Int)
    suspend fun resetArticles(forCountry: String) : Boolean

}

interface IAllNewsRepo {
    suspend fun getNews(country: CountryFilter) : List<ArticleEntity>
    suspend fun getNews(categoryFilter: CategoryFilter ) : List<ArticleEntity>
    //suspend fun getNews(sources: List<Source>) : List<ArticleEntity>
}

@InternalCoroutinesApi
class NewsRepository
    @Inject constructor(
        private val api: INewsAPIClient,
        private val db: AppDatabase,
        private val networkManager: INetworkManger
    ) : INewsHeadlinesRepository, IAllNewsRepo {

    private val scope = CoroutineScope(Dispatchers.IO)


    private fun cache(forCountry: String, data: List<ArticleDTO>) {
        val transformed = data.map { it.toEntity(forCountry) }
        scope.launch {
            db.articlesDao().insertAll(transformed)
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

    override suspend fun getNews(country: CountryFilter): List<ArticleEntity> {
        val local = db.articlesDao().getArticlesByCountry(country.toString())
        return if (local.isEmpty()) { //also check if articles are too old
            val newArticles = api.getHeadlinesByCountry(country.toString()).map {
                it.toEntity(country.toString())
            }
            db.articlesDao().insertAll(newArticles)
            newArticles
        } else {
            local
        }
    }

    override suspend fun getNews(categoryFilter: CategoryFilter): List<ArticleEntity> {
        val local = db.articlesDao().getArticlesByCategory(categoryFilter.toString())
        return if (local.isEmpty()) { //also check if articles are too old
            val newArticles = api.getHeadlinesByCategory(categoryFilter.toQueryParameter()).map {
                it.toEntity(categoryFilter.toQueryParameter())
            }
            db.articlesDao().insertAll(newArticles)
            newArticles
        } else {
            local
        }
    }


}