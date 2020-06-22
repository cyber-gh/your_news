package dev.skyit.yournews.api.client

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.skyit.yournews.api.models.headlines.ArticleDTO
import dev.skyit.yournews.api.models.headlines.NewsListResponse
import dev.skyit.yournews.api.models.sources.SourceExtended
import dev.skyit.yournews.api.models.sources.SourcesResponse
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.QueryMap
import javax.inject.Inject


interface INewsAPIClient {
    interface NewsAPIService {
        @GET("top-headlines")
        suspend fun getHeadlines(@QueryMap options: Map<String, String>) : NewsListResponse

        @GET("everything")
        suspend fun searchArticles(@QueryMap options: Map<String, String>) : NewsListResponse

        @GET("sources")
        suspend fun getSources() : SourcesResponse
    }


    suspend fun getHeadlines(country: String) : List<ArticleDTO>
    suspend fun getHeadlinesByCategory(category: String) : List<ArticleDTO>
    suspend fun getHeadlinesPaged(country: String = "us", pageNumber: Int, pageSize: Int) : List<ArticleDTO>

    suspend fun searchArticles(keyword: String) : List<ArticleDTO>

    suspend fun getAllSources() : List<SourceExtended>
}

class NewsAPIClient @Inject constructor(): INewsAPIClient{

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

    @OptIn(UnstableDefault::class)
    private val newsAPIService: INewsAPIClient.NewsAPIService by lazy{
        Retrofit.Builder()
            .baseUrl(apiRoot)
            .client(httpClient)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
            .create(INewsAPIClient.NewsAPIService::class.java)
    }

    override suspend fun getHeadlines(country: String): List<ArticleDTO> {
        val options = hashMapOf("country" to country)
        return newsAPIService.getHeadlines(options).articles
    }

    override suspend fun getHeadlinesByCategory(category: String): List<ArticleDTO> {
        val options = hashMapOf("category" to category)
        return newsAPIService.getHeadlines(options).articles
    }

    override suspend fun getHeadlinesPaged(country: String, pageNumber: Int, pageSize: Int): List<ArticleDTO> {
        val options = hashMapOf(
            "country" to country,
            "page" to pageNumber.toString(),
            "pageSize" to pageSize.toString())
        return newsAPIService.getHeadlines(options).articles

    }

    override suspend fun searchArticles(keyword: String): List<ArticleDTO> {
        val options = hashMapOf(
            "q" to keyword
        )
        return newsAPIService.searchArticles(options).articles
    }

    override suspend fun getAllSources(): List<SourceExtended> {
        return newsAPIService.getSources().sources
    }


}