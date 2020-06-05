package dev.skyit.yournews.repository.favorites

import dev.skyit.yournews.api.caching.ArticleEntity

interface IFavouriteNewsRepo {
    suspend fun getBookmarkedArticles() : List<ArticleEntity>
    suspend fun addBookmark(articleEntity: ArticleEntity)
}

