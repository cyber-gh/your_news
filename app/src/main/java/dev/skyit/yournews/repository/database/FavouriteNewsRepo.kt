package dev.skyit.yournews.repository.database

import javax.inject.Inject


interface IFavouriteNewsRepo {
    suspend fun getBookmarkedArticles() : List<ArticleEntity>
    suspend fun addBookmark(articleEntity: ArticleEntity)
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

}

