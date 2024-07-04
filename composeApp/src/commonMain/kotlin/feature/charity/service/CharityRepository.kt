package feature.charity.service

import core.data.ApiResponse
import core.data.DataState
import feature.charity.service.resource.remote.CharityRemote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

class CharityRepository(
    private val remote: CharityRemote,
) {
    fun fetchCharities() =
        flow {
            when (val result = remote.fetchCharities()) {
                is ApiResponse.Error -> emit(DataState.Error(result.message))
                is ApiResponse.Success -> {
                    val remoteResult = result.body

                    val latestCharities =
                        remoteResult
                            .sortedByDescending { it.createdAt }
                            .map { it.imageUrl.orEmpty() }

                    emit(DataState.Success(latestCharities))
                }
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)
}
