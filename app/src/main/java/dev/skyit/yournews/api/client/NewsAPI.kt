package dev.skyit.yournews.api.client

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.skyit.yournews.api.models.Article
import dev.skyit.yournews.api.models.NewsHeadlinesResponse
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query


interface INewsAPIClient {
    interface NewsAPIService {
        @GET("top-headlines")
        suspend fun getHeadlines(@Query("country") country: String = "us") : NewsHeadlinesResponse
    }


    suspend fun getHeadlines(country: String) : List<Article>
}

class NewsAPIClient: INewsAPIClient {

    private val apiRoot = "https://newsapi.org/v2/"
    private val contentType =  "application/json".toMediaType()

    private val apiKey = "4da07d6e3e1e42c98e8c640a5f128447"

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original: Request = chain.request()
                val originalHttpUrl: HttpUrl = original.url

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("apikey", apiKey)
                    .build()

                val requestBuilder: Request.Builder = original.newBuilder()
                    .url(url)

                val request: Request = requestBuilder.build()
                chain.proceed(request)
            }.build()
    }

    private val newsAPIService: INewsAPIClient.NewsAPIService by lazy{
        Retrofit.Builder()
            .baseUrl(apiRoot)
            .client(httpClient)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build().create(INewsAPIClient.NewsAPIService::class.java)
    }

    override suspend fun getHeadlines(country: String): List<Article> {
        return newsAPIService.getHeadlines(country).articles
    }


}