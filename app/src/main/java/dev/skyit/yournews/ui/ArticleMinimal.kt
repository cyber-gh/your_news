package dev.skyit.yournews.ui

import com.soywiz.klock.DateTime

data class ArticleMinimal(
    val id: Long = 0,
    val title: String,
    val sources: String,
    val timePublished: String = present.toString(),
    val imageLink: String? = null,
    val url: String
)


val present: DateTime
    get() = DateTime.nowLocal().local