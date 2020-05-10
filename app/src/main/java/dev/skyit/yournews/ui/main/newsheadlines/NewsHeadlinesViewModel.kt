package dev.skyit.yournews.ui.main.newsheadlines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soywiz.klock.*
import dev.skyit.yournews.repository.INewsRepository
import dev.skyit.yournews.ui.ArticleMinimal
import dev.skyit.yournews.ui.present
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class NewsHeadlinesViewModel(
    private val newsRepo: INewsRepository
) : ViewModel() {

    val newsLive = MutableLiveData<List<ArticleMinimal>>()

    fun loadData() {
        viewModelScope.launch {
            kotlin.runCatching {
                newsRepo.getHeadlines("us")
            }.onSuccess {
                val minimal = it.map {
                    val tm = DateTime.parse(it.publishedAt).local
                    ArticleMinimal(it.title, it.source.name, tm.relativeTime(), it.urlToImage)
                }
                newsLive.postValue(minimal)
            }.onFailure {
                //TODO
            }
        }
    }

    fun DateTime.relativeTime() : String {
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