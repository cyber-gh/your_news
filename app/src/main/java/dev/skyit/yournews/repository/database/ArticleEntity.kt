package dev.skyit.yournews.repository.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.skyit.yournews.api.models.Source
import java.io.Serializable

@Entity(tableName = "articles", indices = [Index(value = ["url"], unique = true)])
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val country: String?,
    val category: String?,
    val isBookmarked: Int = 0,

    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: Long, //kind of a bad idea, no timezone support
    val source: Source,
    val title: String,

    val url: String,
    val urlToImage: String?
) : Serializable