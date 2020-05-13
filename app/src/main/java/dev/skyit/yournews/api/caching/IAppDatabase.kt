package dev.skyit.yournews.api.caching

interface IAppDatabase {
    suspend fun insert(articles: List<ArticleEntity>)


}