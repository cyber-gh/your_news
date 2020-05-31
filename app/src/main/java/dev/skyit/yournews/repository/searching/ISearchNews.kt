package dev.skyit.yournews.repository.searching

import dev.skyit.yournews.api.client.INewsAPIClient
import dev.skyit.yournews.api.models.headlines.ArticleDTO
import dev.skyit.yournews.repository.datasource.INewsHeadlines

interface ISearchNews {

    suspend fun searchNews(keyword: String) : List<ArticleDTO>
}

class SearchNewsRepo(
    private val api: INewsAPIClient
): ISearchNews{
    override suspend fun searchNews(keyword: String): List<ArticleDTO> {
        return api.searchArticles(keyword)
    }

}