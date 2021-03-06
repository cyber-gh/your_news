package dev.skyit.yournews.repository.headlines

import dev.skyit.yournews.repository.database.AppDatabase
import dev.skyit.yournews.repository.database.ArticleEntity
import javax.inject.Inject


interface IFavouriteNewsRepo {
    suspend fun getBookmarkedArticles() : List<ArticleEntity>
    suspend fun addBookmark(articleEntity: ArticleEntity)
    suspend fun removeBookmark(articleEntity: ArticleEntity)
}

class FavouriteNewsRepo
    @Inject constructor(
        private val db: AppDatabase
    ) : IFavouriteNewsRepo {
    override suspend fun getBookmarkedArticles(): List<ArticleEntity> {
        return db.articlesDao().getBookmarkedArticles()
    }

    override suspend fun addBookmark(articleEntity: ArticleEntity) {
        db.articlesDao().bookmarkArticle(articleEntity.url)
    }

    override suspend fun removeBookmark(articleEntity: ArticleEntity) {
        db.articlesDao().removeBookmark(articleEntity.url)
    }

}

