package dev.skyit.yournews.repository.datasource

import androidx.paging.ItemKeyedDataSource
import androidx.paging.PageKeyedDataSource
import dev.skyit.yournews.api.client.INewsAPIClient
import dev.skyit.yournews.api.models.headlines.Article
import kotlinx.coroutines.*

class NewsHeadlinesDataSource(
    private val country: String,
    private val newsHeadlines: INewsAPIClient
) : PageKeyedDataSource<Int, Article>() {
    private val scope = CoroutineScope(Dispatchers.IO)
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Article>
    ) {
        scope.launch {
            kotlin.runCatching {
                newsHeadlines.getHeadlinesPaged(country, 1, params.requestedLoadSize)
            }.onSuccess {
                callback.onResult(it,null, 2)
            }.onFailure {
                callback.onResult(emptyList(), null, null)
            }
        }

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
        scope.launch {
            kotlin.runCatching {
                newsHeadlines.getHeadlinesPaged(country, params.key, params.requestedLoadSize)
            }.onSuccess {
                callback.onResult(it, params.key + 1)
            }.onFailure {
                callback.onResult(emptyList(), null)
            }
        }

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
        scope.launch {
            kotlin.runCatching {
                newsHeadlines.getHeadlinesPaged(country, params.key, params.requestedLoadSize)
            }.onSuccess {
                callback.onResult(it, if (params.key - 1 >= 1) params.key - 1 else null)
            }.onFailure {
                callback.onResult(emptyList(), null)
            }
        }
    }

    override fun invalidate() {
        super.invalidate()
        scope.cancel()
    }
}