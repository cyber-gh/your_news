package dev.skyit.yournews.ui.main.bookmarks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.skyit.yournews.repository.converters.toMinimal
import dev.skyit.yournews.repository.headlines.IFavouriteNewsRepo
import dev.skyit.yournews.ui.ArticleMinimal
import kotlinx.coroutines.launch

class NewsBookmarksViewModel
    @ViewModelInject constructor(
        private val favouriteNewsRepo: IFavouriteNewsRepo
    ): ViewModel() {

    val data = MutableLiveData<List<ArticleMinimal>>()

    fun loadData() {
        viewModelScope.launch {
            kotlin.runCatching {
                favouriteNewsRepo.getBookmarkedArticles()
            }.onSuccess {
                val articles = it.map { it.toMinimal() }
                data.postValue(articles)
            }
        }
    }
}