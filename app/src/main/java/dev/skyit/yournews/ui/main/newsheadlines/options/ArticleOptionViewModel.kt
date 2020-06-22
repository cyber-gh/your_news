package dev.skyit.yournews.ui.main.newsheadlines.options

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.skyit.yournews.repository.database.ArticleEntity
import dev.skyit.yournews.repository.headlines.IFavouriteNewsRepo
import dev.skyit.yournews.repository.newsources.INewsSourceRepo
import dev.skyit.yournews.ui.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber

class SharedArticleOptionViewModel
    @ViewModelInject constructor() : ViewModel() {

    val articleDeletedEvent = SingleLiveEvent<Unit>()


}

class ArticleOptionViewModel
    @ViewModelInject constructor(
        private val favouriteNewsRepo: IFavouriteNewsRepo,
        private val sourcesRepo: INewsSourceRepo
    ) : ViewModel() {

    //Temporary fix until Hilt supports AssistedInject
    private lateinit var articleEntity: ArticleEntity

    fun inject(articleEntity: ArticleEntity) {
        this.articleEntity = articleEntity
    }

    fun bookmark() {
        viewModelScope.launch {
            favouriteNewsRepo.addBookmark(articleEntity)
        }
    }

    fun removeBookmark() {
        viewModelScope.launch {
            favouriteNewsRepo.removeBookmark(articleEntity)
        }
    }

    fun getSource() {
        viewModelScope.launch {
            kotlin.runCatching {
                sourcesRepo.getSource(articleEntity.source.name)
            }.onSuccess {
                if (it != null)
                    sourceLinkLive.value = (it.url.replace("http", "https"))
            }.onFailure {
                Timber.e(it)
            }
        }
    }

    val sourceLinkLive = SingleLiveEvent<String>()
}