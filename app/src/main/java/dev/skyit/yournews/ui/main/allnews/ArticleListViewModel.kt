package dev.skyit.yournews.ui.main.allnews

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.skyit.yournews.api.models.CategoryFilter
import dev.skyit.yournews.api.models.CountryFilter
import dev.skyit.yournews.repository.converters.toMinimal
import dev.skyit.yournews.repository.database.ArticleEntity
import dev.skyit.yournews.repository.headlines.IAllNewsRepo
import dev.skyit.yournews.ui.ArticleMinimal
import kotlinx.coroutines.launch
import timber.log.Timber

class ArticleListViewModel
    @ViewModelInject constructor(
        private val newsRepo: IAllNewsRepo
    ) : ViewModel() {
    private var articlesSource: ArticlesSource = ArticlesSource.CategorySource(CategoryFilter.GENERAL)

    fun setSource(articlesSource: ArticlesSource) {
        this.articlesSource = articlesSource
    }

    val articles = MutableLiveData<List<ArticleMinimal>>()



    fun loadArticles() {
        viewModelScope.launch {
            kotlin.runCatching {
                getArticles(articlesSource)
            }.onSuccess {
                val data = it.map { it.toMinimal() }
                articles.postValue(data)
            }.onFailure {
                Timber.e(it)
            }
        }
    }

    private suspend fun getArticles(src: ArticlesSource) : List<ArticleEntity> {
        return when(src) {
            is ArticlesSource.CountrySource -> newsRepo.getNews(src.countryFilter)
            is ArticlesSource.CategorySource -> newsRepo.getNews(src.categoryFilter)
            else -> newsRepo.getNews(CategoryFilter.GENERAL)
        }
    }

}


sealed class ArticlesSource {
    companion object {
        val OPTIONS : List<ArticlesSource> =
                CategoryFilter.values().map { CategorySource(it) }
    }

    data class CountrySource(val countryFilter: CountryFilter) : ArticlesSource()
    data class CategorySource(val categoryFilter: CategoryFilter) : ArticlesSource()

    val name: String
    get() {
        return when(this) {
            is CountrySource -> this.countryFilter.toString()
            is CategorySource -> this.categoryFilter.toString()
        }
    }
}
