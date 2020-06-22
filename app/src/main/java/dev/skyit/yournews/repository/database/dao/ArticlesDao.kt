package dev.skyit.yournews.repository.database.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.skyit.yournews.repository.database.ArticleEntity


@Dao
interface ArticlesDao {

    @Query("select * from articles where isBookmarked = 1")
    suspend fun getBookmarkedArticles() : List<ArticleEntity>

    @Query("update articles set isBookmarked = 1 where url = :url")
    suspend fun bookmarkArticle(url : String)

    @Query("update articles set isBookmarked = 0 where url = :url")
    suspend fun removeBookmark(url: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<ArticleEntity>)

    @Query("SELECT * FROM articles where country = :country order by publishedAt desc")
    fun articlesDataSource(country: String): DataSource.Factory<Int, ArticleEntity>

    @Query("Select count(*) from articles where country = :country")
    suspend fun articlesNr(country: String) : Int

    @Query("delete from articles where country = :country and isBookmarked = 0")
    suspend fun deleteByCountry(country: String)

    @Query("select * from articles where url = :url")
    suspend fun findArticle(url: String) : ArticleEntity?


    @Query("select * from articles where category = :category")
    suspend fun getArticlesByCategory(category: String) : List<ArticleEntity>


    @Query("select * from articles where category = :country")
    suspend fun getArticlesByCountry(country: String) : List<ArticleEntity>

    @Query("delete from articles where category = :category")
    suspend fun deleteArticlesByCategory(category: String)
}