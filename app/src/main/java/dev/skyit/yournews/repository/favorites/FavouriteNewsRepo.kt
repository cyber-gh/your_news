package dev.skyit.yournews.repository.favorites

import androidx.paging.DataSource
import dev.skyit.yournews.repository.caching.ArticleEntity


interface IFavouriteNewsRepo {
    suspend fun getBookmarkedArticles() : List<ArticleEntity>
    suspend fun addBookmark(articleEntity: ArticleEntity)
}

class FavouriteNewsRepo(
    private val db: AppDatabase
) : IFavouriteNewsRepo {
    override suspend fun getBookmarkedArticles(): List<ArticleEntity> {
        return db.articlesDao().getBookmarkedArticles()
    }

    override suspend fun addBookmark(articleEntity: ArticleEntity) {
        db.articlesDao().addArticle(articleEntity)
    }

}

