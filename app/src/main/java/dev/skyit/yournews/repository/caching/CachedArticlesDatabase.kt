package dev.skyit.yournews.repository.caching

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(SpecialTypeConverters::class)
@Database(
    entities = [ArticleEntity::class],
    version = 5,
    exportSchema = false
)
abstract class CachedArticlesDatabase : RoomDatabase(), IArticlesDatabase {

    companion object {
        private const val dbName = "news-cache.db"

        @Volatile
        private var instance: CachedArticlesDatabase? = null
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
            CachedArticlesDatabase::class.java,
            dbName
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {

        })
            .build()
    }
}