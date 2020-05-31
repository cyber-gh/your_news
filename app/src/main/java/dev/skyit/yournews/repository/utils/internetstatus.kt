package dev.skyit.yournews.repository.utils

import dev.skyit.yournews.api.INetworkManger
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

interface IInternetReturned {
    val reconnectedToInternet: Flow<Unit>
}

@InternalCoroutinesApi
class InternetStatusRepo(
    private val networkManager: INetworkManger
): IInternetReturned {
    // the first one is irrelevant, after that we take only those who give true which means the network is available

    override val reconnectedToInternet: Flow<Unit> = networkManager.hasInternet.drop(1).filter {
        it
    }.map { Unit }
}