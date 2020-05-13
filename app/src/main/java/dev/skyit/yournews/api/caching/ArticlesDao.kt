package dev.skyit.yournews.api.caching

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomArticlesDao {


    @Insert
    suspend fun insertAll(vararg articleEntity: ArticleEntity) : List<Long>

    @Query("SELECT * FROM articles where country = :country order by publishedAt desc")
    fun articlesDataSource(country: String): DataSource.Factory<Int, ArticleEntity>
}