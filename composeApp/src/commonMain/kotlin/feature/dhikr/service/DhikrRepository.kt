package feature.dhikr.service

import androidx.compose.ui.util.fastFirst
import core.data.ApiResponse
import core.data.DataState
import feature.dhikr.service.entity.AsmaulHusnaRealm
import feature.dhikr.service.entity.DhikrRealm
import feature.dhikr.service.entity.DuaCategoryRealm
import feature.dhikr.service.entity.DuaRealm
import feature.dhikr.service.mapper.mapToDuaRealm
import feature.dhikr.service.mapper.mapToModel
import feature.dhikr.service.mapper.mapToRealm
import feature.dhikr.service.model.Dhikr
import feature.dhikr.service.model.DhikrType
import feature.dhikr.service.source.remote.DhikrRemote
import feature.other.service.AppRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

class DhikrRepository(
    private val appRepository: AppRepository,
    private val remote: DhikrRemote,
    private val realm: Realm,
) {
    fun fetchDhikr(dhikrType: DhikrType) =
        flow {
            val setting = appRepository.getSetting()
            val localDhikrs: RealmResults<DhikrRealm> =
                realm.query<DhikrRealm>("type == $0", dhikrType.name).find()

            if (localDhikrs.isEmpty()) {
                when (val result = remote.fetchDhikr(dhikrType)) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val remoteDhikr = result.body

                        realm.writeBlocking {
                            remoteDhikr.forEach { dhikr ->
                                copyToRealm(
                                    dhikr.mapToRealm(dhikrType),
                                )
                            }
                        }

                        val dhikrs =
                            realm
                                .query<DhikrRealm>("type == $0", dhikrType.name)
                                .find()
                                .map { it.mapToModel(setting) }
                                .sortedBy { it.id }
                        emit(DataState.Success(dhikrs))
                    }
                }
            } else {
                val dhikrs =
                    realm
                        .query<DhikrRealm>("type == $0", dhikrType.name)
                        .find()
                        .map { it.mapToModel(setting) }
                        .sortedBy { it.id }
                emit(DataState.Success(dhikrs))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)

    fun updateDhikrCount(
        dhikrType: DhikrType,
        dhikr: Dhikr,
    ) {
        realm.writeBlocking {
            val localDhikr =
                this.query<DhikrRealm>("type == $0", dhikrType.name).find().fastFirst {
                    it.id == dhikr.id
                }

            localDhikr.count += 1
        }
    }

    fun resetDhikrCount(dhikrType: DhikrType) {
        realm.writeBlocking {
            val localDhikrs = this.query<DhikrRealm>("type == $0", dhikrType.name).find()

            localDhikrs.forEach {
                it.count = 0
            }
        }
    }

    fun fetchDuaCategories(tagFilter: String = "") =
        flow {
            val setting = appRepository.getSetting()
            val localDuaCategories = realm.query<DuaCategoryRealm>().find()

            if (localDuaCategories.isEmpty()) {
                when (val result = remote.fetchDuaCategories()) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val remoteResult = result.body

                        realm.writeBlocking {
                            remoteResult.forEach { category ->
                                copyToRealm(
                                    category.mapToRealm(),
                                )
                            }
                        }

                        val latestCategories =
                            realm
                                .query<DuaCategoryRealm>()
                                .find()
                                .apply {
                                    if (tagFilter.isNotEmpty()) filter { it.tag == tagFilter }
                                }.map { it.mapToModel(setting) }
                                .sortedBy { it.title }

                        emit(DataState.Success(latestCategories))
                    }
                }
            } else {
                val latestCategories =
                    realm
                        .query<DuaCategoryRealm>()
                        .find()
                        .apply {
                            if (tagFilter.isNotEmpty()) filter { it.tag == tagFilter }
                        }.map { it.mapToModel(setting) }
                        .sortedBy { it.title }

                emit(DataState.Success(latestCategories))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)

    fun fetchDuaByTag(tag: String) =
        flow {
            val setting = appRepository.getSetting()
            val localDuas = realm.query<DuaRealm>().find()

            if (localDuas.isEmpty()) {
                when (val result = remote.fetchDuaSunnah()) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val remoteResult = result.body

                        realm.writeBlocking {
                            remoteResult.forEach { dua ->
                                copyToRealm(
                                    dua.mapToDuaRealm(),
                                )
                            }
                        }

                        val latestDuas =
                            realm
                                .query<DuaRealm>()
                                .find()
                                .filter { it.tag.contains(tag) }
                                .map { it.mapToModel(setting) }
                                .sortedBy { it.title }

                        emit(DataState.Success(latestDuas))
                    }
                }
            } else {
                val latestDuas =
                    realm
                        .query<DuaRealm>()
                        .find()
                        .filter { it.tag.contains(tag) }
                        .map { it.mapToModel(setting) }
                        .sortedBy { it.title }

                emit(DataState.Success(latestDuas))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)

    fun fetchAsmaulHusna() =
        flow {
            val setting = appRepository.getSetting()
            val localDhikrs = realm.query<AsmaulHusnaRealm>().find()

            if (localDhikrs.isEmpty()) {
                when (val result = remote.fetchAsmaulHusna()) {
                    is ApiResponse.Error -> emit(DataState.Error(result.message))
                    is ApiResponse.Success -> {
                        val remoteResult = result.body

                        realm.writeBlocking {
                            remoteResult.forEach { dhikr ->
                                copyToRealm(
                                    dhikr.mapToRealm(),
                                )
                            }
                        }

                        val latestDhikrs =
                            realm
                                .query<AsmaulHusnaRealm>()
                                .find()
                                .map { it.mapToModel(setting) }
                                .sortedBy { it.id }

                        emit(DataState.Success(latestDhikrs))
                    }
                }
            } else {
                val latestDhikrs =
                    realm
                        .query<AsmaulHusnaRealm>()
                        .find()
                        .map { it.mapToModel(setting) }
                        .sortedBy { it.id }

                emit(DataState.Success(latestDhikrs))
            }
        }.onStart { emit(DataState.Loading) }
            .catch { emit(DataState.Error(it.message.orEmpty())) }
            .flowOn(Dispatchers.IO)
}
