package dev.skyit.yournews.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.skyit.yournews.api.models.sources.SourceExtended

@Dao
interface SourcesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sourceExtended: List<SourceExtended>)

    @Query("select * from sources where lower(name) = :name")
    suspend fun findSourceBy(name: String) : SourceExtended?

    @Query("select count(*) from sources")
    suspend fun countSources() : Long

    @Query("select * from sources where language = :langCode")
    suspend fun getSourcesByLang(langCode: String) : List<SourceExtended>
}