package dev.skyit.yournews.repository.database.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.skyit.yournews.repository.database.ArticleEntity


@Dao
interface ArticlesDao {

    @Query("select * from articles where isBookmarked = 1")
    suspend fun getBookmarkedArticles() : List<ArticleEntity>

    @Query("update articles set isBookmarked = 1 where url = :url")
    suspend fun bookmarkArticle(url : String)

    @Insert
    suspend fun insertAll(vararg articles: ArticleEntity)

    @Query("SELECT * FROM articles where country = :country order by publishedAt desc")
    fun articlesDataSource(country: String): DataSource.Factory<Int, ArticleEntity>

    @Query("Select count(*) from articles where country = :country")
    suspend fun articlesNr(country: String) : Int

    @Query("delete from articles where country = :country and isBookmarked = 0")
    suspend fun deleteByCountry(country: String)

    @Query("select * from articles where url = :url")
    suspend fun findArticle(url: String) : ArticleEntity?
}