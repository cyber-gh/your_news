package dev.skyit.yournews.ui.main.newsheadlines

import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.soywiz.klock.*
import dev.skyit.yournews.api.models.headlines.Article
import dev.skyit.yournews.repository.INewsRepository
import dev.skyit.yournews.ui.ArticleMinimal
import dev.skyit.yournews.ui.present
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class NewsHeadlinesViewModel(
    private val newsRepo: INewsRepository
) : ViewModel() {

    var newsPagedLive : LiveData<PagedList<ArticleMinimal>>

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, ArticleMinimal> {

        val dataSourceFactory =newsRepo.headlinesDataSource("us").map {
            it.toMinimal()
        }
        return LivePagedListBuilder(dataSourceFactory, config)
    }


    init {

        val config = PagedList.Config.Builder()
            .setPageSize(10)
            .setEnablePlaceholders(false)
            .build()
        newsPagedLive = initializedPagedListBuilder(config).build()
    }
    
    private fun Article.toMinimal(): ArticleMinimal {
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