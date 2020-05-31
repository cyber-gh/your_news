package dev.skyit.yournews.ui.main.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.skyit.yournews.api.models.headlines.ArticleDTO
import dev.skyit.yournews.repository.searching.ISearchNews
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchNewsViewModel(
    private val repo: ISearchNews
) : ViewModel() {

    val searchResults = MutableLiveData<List<ArticleDTO>>()

    private val searchQFlow = Channel<String>()

    init {
        viewModelScope.launch {
            searchQFlow.consumeAsFlow().debounce(500)
                .filter { it.isNotBlank() }
                .collect {
                updateResults(it)
            }
        }
    }

    private suspend fun updateResults(query: String) {
        kotlin.runCatching {
            repo.searchNews(query)
        }.onSuccess {
            searchResults.postValue(it)
        }.onFailure {
            Timber.e(it, "API error")
        }
    }

    fun newSearch(keyword: String) {
        viewModelScope.launch {
            searchQFlow.send(keyword)
        }
    }


}