package dev.skyit.yournews.repository.converters

import com.soywiz.klock.DateTime
import dev.skyit.yournews.api.caching.ArticleEntity
import dev.skyit.yournews.api.models.headlines.ArticleDTO

fun ArticleDTO.toEntity(country: String? = null, category: String? = null) : ArticleEntity {
    return ArticleEntity(
        country = country,
        author = author,
        publishedAt = DateTime.parse(publishedAt).local.unixMillisLong,
        content = content,
        description = description,
        source = source,
        title = title,
        url = url,
        urlToImage = urlToImage,
        category = category

    )
}

fun ArticleEntity.toArticle() : ArticleDTO {
    return ArticleDTO(
        author = author,
        content = content,
        description = description,
        publishedAt = DateTime.fromUnix(publishedAt).toString(),
        source = source,
        title = title,
        urlToImage = urlToImage,
        url = url
    )
}