package dev.skyit.yournews.ui.main.newsheadlines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.soywiz.klock.*
import dev.skyit.yournews.api.models.headlines.ArticleDTO
import dev.skyit.yournews.repository.INewsHeadlinesRepository
import dev.skyit.yournews.ui.ArticleMinimal
import dev.skyit.yournews.ui.present
import dev.skyit.yournews.ui.utils.SingleLiveEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class NewsHeadlinesViewModel(
    private val newsRepo: INewsHeadlinesRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            newsRepo.reconnectedToInternet.collect {
                internetReconnected.postValue(Unit)
            }
        }
    }

    val newsPagedLive : LiveData<PagedList<ArticleMinimal>>

    val refreshStatusLive = SingleLiveEvent<LoadStatus>().apply { value = LoadStatus.IDLE }

    val internetReconnected = MutableLiveData<Unit>()

    private val countryName: String = "us"
    private val pageSize: Int = 5

    fun refreshList() {
        refreshStatusLive.value = LoadStatus.REFRESHING
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

        val dataSourceFactory =newsRepo.headlinesDataSource(countryName).map {
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
            .setEnablePlaceholders(false)
            .build()
        newsPagedLive = initializedPagedListBuilder(config).build()
    }

    private fun ArticleDTO.toMinimal(): ArticleMinimal {
        val tm = DateTime.parse(publishedAt).local
        return ArticleMinimal(title, source.name, tm.relativeTime(), urlToImage)
    }

    private fun DateTime.relativeTime() : String {
        val diff = present - this
        if (diff < 1.hours) {
            return "${diff.minutes.roundToInt()} minutes ago"
        }
        if (diff < 1.days) {
            return "${diff.hours.roundToInt()} hours ago"
        }

        if (diff < 2.days) {
            return "Yesterday"
        }

        if (diff < 1.weeks) {
            return "${diff.days.roundToInt()} days ago"
        }

        if (diff < 5.weeks) {
            return "${diff.weeks.roundToInt()} weeks ago"
        }

        return "${(diff.weeks / 5).roundToInt()} months ago"
    }

}

enum class LoadStatus{
    REFRESHING,
    FAILED,
    COMPLETED,
    IDLE
}