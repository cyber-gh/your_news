package dev.skyit.yournews.dependencies

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.skyit.yournews.api.INetworkManger
import dev.skyit.yournews.api.NetworkManager
import dev.skyit.yournews.api.client.INewsAPIClient
import dev.skyit.yournews.api.client.NewsAPIClient
import dev.skyit.yournews.repository.database.AppDatabase
import dev.skyit.yournews.repository.headlines.FavouriteNewsRepo
import dev.skyit.yournews.repository.headlines.IFavouriteNewsRepo
import dev.skyit.yournews.repository.headlines.INewsHeadlinesRepository
import dev.skyit.yournews.repository.headlines.NewsRepository
import dev.skyit.yournews.repository.newsources.INewsSourceRepo
import dev.skyit.yournews.repository.newsources.NewsSourceRepo
import dev.skyit.yournews.repository.preferences.IUserPreferences
import dev.skyit.yournews.repository.preferences.UserPreferences
import dev.skyit.yournews.repository.searching.ISearchNews
import dev.skyit.yournews.repository.searching.SearchNewsRepo
import dev.skyit.yournews.repository.utils.IInternetReturned
import dev.skyit.yournews.repository.utils.InternetStatusRepo
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class APIModule {

    @Singleton
    @Binds
    abstract fun bindNewsClient(api : NewsAPIClient) : INewsAPIClient
}

@Module
@InstallIn(ApplicationComponent::class)
abstract class NetworkManagerModule {

    @Singleton
    @Binds
    abstract fun bindNetworkManager(manager: NetworkManager) : INetworkManger

    @OptIn(InternalCoroutinesApi::class)
    @Singleton
    @Binds
    abstract fun bindInternetReturned(internetReturned : InternetStatusRepo) : IInternetReturned
}

@Module
@InstallIn(ApplicationComponent::class)
abstract class PreferencesModule {

    @Singleton
    @Binds
    abstract fun bindPreferences(preferences: UserPreferences) : IUserPreferences
}

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context) : AppDatabase = AppDatabase.invoke(context)
}

@Module
@InstallIn(ApplicationComponent::class)
abstract class ArticlesRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindSearchNews(searchNewsRepo: SearchNewsRepo) : ISearchNews

    @OptIn(InternalCoroutinesApi::class)
    @Singleton
    @Binds
    abstract fun bindNewsHeadlineRepo(newsRepo: NewsRepository) : INewsHeadlinesRepository

    @Singleton
    @Binds
    abstract fun bindFavouritenews(favouriteNewsRepo: FavouriteNewsRepo) : IFavouriteNewsRepo

    @Singleton
    @Binds
    abstract fun bindSourcesRepo(sourcesRepo: NewsSourceRepo) : INewsSourceRepo
}

