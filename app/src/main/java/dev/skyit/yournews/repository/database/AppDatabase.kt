package dev.skyit.yournews.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.skyit.yournews.api.models.sources.SourceExtended
import dev.skyit.yournews.repository.database.dao.ArticlesDao
import dev.skyit.yournews.repository.database.dao.SourcesDao


@TypeConverters(SpecialTypeConverters::class)
@Database(
    entities = [ArticleEntity::class, SourceExtended::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {


    abstract fun articlesDao(): ArticlesDao

    abstract fun sourcesDao(): SourcesDao

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