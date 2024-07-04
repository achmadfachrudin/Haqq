package feature.study.di

import core.data.NetworkSource
import feature.study.screen.ChannelListScreenModel
import feature.study.screen.NoteDetailScreenModel
import feature.study.screen.NoteListScreenModel
import feature.study.screen.VideoListScreenModel
import feature.study.service.StudyRepository
import feature.study.service.source.remote.StudyRemote
import feature.study.service.source.remote.StudyRemoteImp
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val studyFeatureModule =
    module {
        factory { ChannelListScreenModel(get()) }
        factory { VideoListScreenModel(get()) }
        factory { NoteListScreenModel(get()) }
        factory { NoteDetailScreenModel(get()) }

        single<StudyRemote> {
            StudyRemoteImp(
                get(named(NetworkSource.SUPABASE)),
                get(named(NetworkSource.YOUTUBE)),
            )
        }
        singleOf(::StudyRepository)
    }
