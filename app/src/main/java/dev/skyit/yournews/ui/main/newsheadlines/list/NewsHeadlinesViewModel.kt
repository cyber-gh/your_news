package dev.skyit.yournews.ui.main.newsheadlines.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.soywiz.klock.*
import dev.skyit.yournews.repository.caching.ArticleEntity
import dev.skyit.yournews.repository.converters.toMinimal
import dev.skyit.yournews.repository.headlines.INewsHeadlinesRepository
import dev.skyit.yournews.repository.utils.IInternetReturned
import dev.skyit.yournews.ui.ArticleMinimal
import dev.skyit.yournews.ui.utils.SingleLiveEvent
import dev.skyit.yournews.ui.utils.relativeTime
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class NewsHeadlinesViewModel(
    private val newsRepo: INewsHeadlinesRepository,
    private val networkManager: IInternetReturned
) : ViewModel() {

    init {
        viewModelScope.launch {
            networkManager.reconnectedToInternet.collect {
                internetReconnected.postValue(Unit)
            }
        }
    }

    val newsPagedLive : LiveData<PagedList<ArticleMinimal>>

    val refreshStatusLive = SingleLiveEvent<LoadStatus>().apply { value =
        LoadStatus.IDLE
    }

    val internetReconnected = MutableLiveData<Unit>()

    private val countryName: String = "us"
    private val pageSize: Int = 10

    fun refreshList() {
        refreshStatusLive.value =
            LoadStatus.REFRESHING
        viewModelScope.launch {
            val didRefresh = newsRepo.resetArticles(countryName)
            if (didRefresh) {
                refreshStatusLive.postValue(LoadStatus.COMPLETED)
            } else {
                refreshStatusLive.postValue(LoadStatus.FAILED)
            }
        }
    }

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, ArticleMinimal> {

        val dataSourceFactory = newsRepo.headlinesDataSource(countryName).map {
            it.toMinimal()
        }
        return LivePagedListBuilder(dataSourceFactory, config).setBoundaryCallback( object : PagedList.BoundaryCallback<ArticleMinimal>(){
            override fun onItemAtEndLoaded(itemAtEnd: ArticleMinimal) {
                super.onItemAtEndLoaded(itemAtEnd)
                newsRepo.loadNextPage(countryName, pageSize)
            }

            override fun onZeroItemsLoaded() {
                super.onZeroItemsLoaded()
                newsRepo.loadNextPage(countryName, pageSize)
            }
        })
    }


    init {

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setEnablePlaceholders(true)
            .build()
        newsPagedLive = initializedPagedListBuilder(config).build()
    }



}

enum class LoadStatus{
    REFRESHING,
    FAILED,
    COMPLETED,
    IDLE
}