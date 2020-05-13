package dev.skyit.yournews.api.caching

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomArticlesDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg articleEntity: ArticleEntity) : List<Long>

    @Query("SELECT * FROM articles where country = :country order by publishedAt desc")
    fun articlesDataSource(country: String): DataSource.Factory<Int, ArticleEntity>

    @Query("Select count(*) from articles where country = :country")
    suspend fun articlesNr(country: String) : Int

    @Query("delete from articles where country = :country")
    suspend fun deleteByCountry(country: String)
}