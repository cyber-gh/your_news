package dev.skyit.yournews.repository.searching

import dev.skyit.yournews.api.client.INewsAPIClient
import dev.skyit.yournews.api.models.headlines.ArticleDTO
import dev.skyit.yournews.repository.datasource.INewsHeadlines
import javax.inject.Inject

interface ISearchNews {

    suspend fun searchNews(keyword: String) : List<ArticleDTO>
}

class SearchNewsRepo
    @Inject constructor(
        private val api: INewsAPIClient
    ): ISearchNews{
    override suspend fun searchNews(keyword: String): List<ArticleDTO> {
        return api.searchArticles(keyword)
    }

}