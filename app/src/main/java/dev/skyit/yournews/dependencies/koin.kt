package dev.skyit.yournews.dependencies

import dev.skyit.yournews.api.INetworkManger
import dev.skyit.yournews.api.NetworkManager
import dev.skyit.yournews.api.caching.ArticlesDatabase
import dev.skyit.yournews.api.client.INewsAPIClient
import dev.skyit.yournews.api.client.NewsAPIClient
import dev.skyit.yournews.repository.INewsHeadlinesRepository
import dev.skyit.yournews.repository.NewsRepository
import dev.skyit.yournews.repository.searching.ISearchNews
import dev.skyit.yournews.repository.searching.SearchNewsRepo
import dev.skyit.yournews.ui.main.newsheadlines.NewsHeadlinesViewModel
import dev.skyit.yournews.ui.main.search.SearchNewsViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module


val viewModelsModule = module {
    viewModel {
        NewsHeadlinesViewModel(get())
    }

    viewModel {
        SearchNewsViewModel(get())
    }
}

val apiModule = module {
    single<INewsAPIClient>{
        NewsAPIClient()
    }

    single<INetworkManger>{
        NetworkManager(androidContext())
    }
}

@InternalCoroutinesApi
val repositoryModule = module {
    single<ISearchNews> {
        SearchNewsRepo(get())
    }

    single {
        NewsRepository(get(), get(), get(), get())
    } bind ISearchNews::class bind INewsHeadlinesRepository::class


}

val databaseModule = module {
    single {
        ArticlesDatabase(androidContext())
    }
}