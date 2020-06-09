package dev.skyit.yournews.dependencies

import dev.skyit.yournews.api.INetworkManger
import dev.skyit.yournews.api.NetworkManager
import dev.skyit.yournews.repository.caching.CachedArticlesDatabase
import dev.skyit.yournews.repository.caching.IArticlesDatabase
import dev.skyit.yournews.api.client.INewsAPIClient
import dev.skyit.yournews.api.client.NewsAPIClient
import dev.skyit.yournews.repository.caching.ArticleEntity
import dev.skyit.yournews.repository.favorites.AppDatabase
import dev.skyit.yournews.repository.favorites.FavouriteNewsRepo
import dev.skyit.yournews.repository.favorites.IFavouriteNewsRepo
import dev.skyit.yournews.repository.headlines.INewsHeadlinesRepository
import dev.skyit.yournews.repository.headlines.NewsRepository
import dev.skyit.yournews.repository.searching.ISearchNews
import dev.skyit.yournews.repository.searching.SearchNewsRepo
import dev.skyit.yournews.repository.utils.IInternetReturned
import dev.skyit.yournews.repository.utils.InternetStatusRepo
import dev.skyit.yournews.ui.main.bookmarks.NewsBookmarksViewModel
import dev.skyit.yournews.ui.main.newsheadlines.list.NewsHeadlinesViewModel
import dev.skyit.yournews.ui.main.newsheadlines.options.ArticleOptionViewModel
import dev.skyit.yournews.ui.main.search.SearchNewsViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelsModule = module {
    viewModel {
        NewsHeadlinesViewModel(
            get(),
            get()
        )
    }

    viewModel {
        SearchNewsViewModel(get())
    }

    viewModel {
        NewsBookmarksViewModel(get())
    }

    viewModel { (data: ArticleEntity) ->
        ArticleOptionViewModel(data, get())
    }
}

@InternalCoroutinesApi
val apiModule = module {
    single<INewsAPIClient>{
        NewsAPIClient()
    }

    single<INetworkManger>{
        NetworkManager(androidContext())
    }

    single<IInternetReturned> {
        InternetStatusRepo(get())
    }
}

@InternalCoroutinesApi
val repositoryModule = module {
    single<ISearchNews> {
        SearchNewsRepo(get())
    }

    single<INewsHeadlinesRepository> {
        NewsRepository(
            get(),
            get(),
            get()
        )
    }

    single<IFavouriteNewsRepo> {
        FavouriteNewsRepo(get())
    }


}

val databaseModule = module {
    single<IArticlesDatabase> {
        CachedArticlesDatabase(androidContext())
    }

    single {
        AppDatabase(androidContext())
    }
}