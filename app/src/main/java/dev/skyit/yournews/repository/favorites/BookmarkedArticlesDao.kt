package dev.skyit.yournews.repository.favorites

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.skyit.yournews.repository.caching.ArticleEntity


@Dao
interface BookmarkedArticlesDao {

    @Query("select * from articles")
    suspend fun getBookmarkedArticles() : List<ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addArticle(vararg articleEntity: ArticleEntity)
}