package dev.skyit.yournews.ui.main.newsheadlines.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.skyit.yournews.repository.caching.ArticleEntity
import dev.skyit.yournews.repository.favorites.FavouriteNewsRepo
import dev.skyit.yournews.repository.favorites.IFavouriteNewsRepo
import kotlinx.coroutines.launch

class ArticleOptionViewModel(
    val article: ArticleEntity,
    private val favouriteNewsRepo: IFavouriteNewsRepo
) : ViewModel() {



    fun bookmark() {
        viewModelScope.launch {
            favouriteNewsRepo.addBookmark(article)
        }
    }
}