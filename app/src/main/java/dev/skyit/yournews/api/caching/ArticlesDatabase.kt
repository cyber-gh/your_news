package dev.skyit.yournews.api.caching

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(SpecialTypeConverters::class)
@Database(
    entities = arrayOf(ArticleEntity::class),
    version = 1,
    exportSchema = false
)
abstract class ArticlesDatabase : RoomDatabase() {


    abstract fun articlesDao(): RoomArticlesDao

    companion object {
        private const val dbName = "news.db"

        @Volatile
        private var instance: ArticlesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
                instance
                    ?: buildDatabase(
                        context
                    )
            }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            ArticlesDatabase::class.java,
            dbName
        ).addCallback(object : RoomDatabase.Callback() {

        })
            .build()
    }
}