package dev.skyit.yournews.ui.main.newsheadlines.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import dev.skyit.yournews.api.models.CountryFilter
import dev.skyit.yournews.repository.converters.toMinimal
import dev.skyit.yournews.repository.headlines.INewsHeadlinesRepository
import dev.skyit.yournews.repository.preferences.IUserPreferences
import dev.skyit.yournews.repository.utils.IInternetReturned
import dev.skyit.yournews.ui.ArticleMinimal
import dev.skyit.yournews.ui.utils.SingleLiveEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class NewsHeadlinesViewModel
    @ViewModelInject constructor(
        private val newsRepo: INewsHeadlinesRepository,
        private val networkManager: IInternetReturned,
        private val userPreferences: IUserPreferences
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

    private var countryName: String = userPreferences.preferredCountry
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

    fun reloadSettings() {
        if (countryName != userPreferences.preferredCountry) {
            countryName = userPreferences.preferredCountry
            refreshList()
        }
    }

    val useMiniCards: Boolean
    get() = userPreferences.useMiniCards



}

enum class LoadStatus{
    REFRESHING,
    FAILED,
    COMPLETED,
    IDLE
}