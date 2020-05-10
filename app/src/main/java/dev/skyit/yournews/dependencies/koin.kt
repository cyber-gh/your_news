package dev.skyit.yournews.dependencies

import dev.skyit.yournews.api.client.INewsAPIClient
import dev.skyit.yournews.api.client.NewsAPIClient
import dev.skyit.yournews.repository.INewsRepository
import dev.skyit.yournews.repository.NewsRepository
import dev.skyit.yournews.ui.main.newsheadlines.NewsHeadlinesViewModel
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
}

val repositoryModule = module {
    single<INewsRepository> {
        NewsRepository(get())
    }
}