package feature.article.di

import core.data.NetworkSource
import feature.article.screen.ArticleListScreenModel
import feature.article.service.ArticleRepository
import feature.article.service.source.remote.ArticleRemote
import feature.article.service.source.remote.ArticleRemoteImp
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val articleFeatureModule =
    module {
        factory { ArticleListScreenModel(get()) }

        single<ArticleRemote> {
            ArticleRemoteImp(
                get(named(NetworkSource.ARTICLE)),
                get(named(NetworkSource.SUPABASE)),
            )
        }

        singleOf(::ArticleRepository)
    }
