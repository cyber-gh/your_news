package dev.skyit.yournews.repository.favorites

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.skyit.yournews.repository.caching.ArticleEntity
import dev.skyit.yournews.repository.caching.IArticlesDatabase
import dev.skyit.yournews.repository.caching.RoomArticlesDao
import dev.skyit.yournews.repository.caching.SpecialTypeConverters


@TypeConverters(SpecialTypeConverters::class)
@Database(
    entities = [ArticleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {


    abstract fun articlesDao(): BookmarkedArticlesDao

    companion object {
        private const val dbName = "news.db"

        @Volatile
        private var instance: AppDatabase? = null
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
            AppDatabase::class.java,
            dbName
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {

            })
            .build()
    }
}