package dev.skyit.yournews.repository.newsources

import dev.skyit.yournews.api.client.INewsAPIClient
import dev.skyit.yournews.api.models.sources.SourceExtended
import dev.skyit.yournews.repository.database.AppDatabase
import javax.inject.Inject

interface INewsSourceRepo {
    suspend fun getSource(name: String) : SourceExtended?
}

class NewsSourceRepo
    @Inject constructor(
        private val api : INewsAPIClient,
        private val db: AppDatabase
    ): INewsSourceRepo {
    override suspend fun getSource(name: String): SourceExtended? {

        if (isDatabaseEmpty()) {
            val allSources = api.getAllSources()
            db.sourcesDao().insertAll(allSources)
        }

        return db.sourcesDao().findSourceBy(name.toLowerCase())
    }


    private suspend fun isDatabaseEmpty() : Boolean {
        return db.sourcesDao().countSources() == 0L
    }
}