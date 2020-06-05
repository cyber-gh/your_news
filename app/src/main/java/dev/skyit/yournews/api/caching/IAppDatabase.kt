package dev.skyit.yournews.api.caching

interface IAppDatabase {
    fun articlesDao(): RoomArticlesDao
}