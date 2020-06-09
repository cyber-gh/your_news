package dev.skyit.yournews.repository.caching

interface IArticlesDatabase {
    fun articlesDao(): RoomArticlesDao
}