package dev.skyit.yournews.dependencies

import dev.skyit.yournews.api.INetworkManger
import dev.skyit.yournews.api.NetworkManager
import dev.skyit.yournews.api.caching.ArticlesDatabase
import dev.skyit.yournews.api.client.INewsAPIClient
import dev.skyit.yournews.api.client.NewsAPIClient
import dev.skyit.yournews.repository.INewsRepository
import dev.skyit.yournews.repository.NewsRepository
import dev.skyit.yournews.ui.main.newsheadlines.NewsHeadlinesViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelsModule = module {
    viewModel {
        NewsHeadlinesViewModel(get())
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
    single<INewsRepository> {
        NewsRepository(get(), get(), get())
    }
}

val databaseModule = module {
    single {
        ArticlesDatabase(androidContext())
    }
}