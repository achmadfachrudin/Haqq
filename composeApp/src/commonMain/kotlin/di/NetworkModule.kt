package di

import core.data.NetworkSource
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule =
    module {
        single(named(NetworkSource.ALADHAN)) { createHttpClient(NetworkSource.ALADHAN) }
        single(named(NetworkSource.ARTICLE)) { createHttpClient(NetworkSource.ARTICLE) }
        single(named(NetworkSource.QURAN)) { createHttpClient(NetworkSource.QURAN) }
        single(named(NetworkSource.SUPABASE)) { createHttpClient(NetworkSource.SUPABASE) }
        single(named(NetworkSource.YOUTUBE)) { createHttpClient(NetworkSource.YOUTUBE) }
    }

private fun createHttpClient(networkSource: NetworkSource): HttpClient =
    HttpClient {
        install(DefaultRequest) {
            when (networkSource) {
                NetworkSource.ALADHAN -> {
                    url(NetworkSource.Aladhan.BASE_URL)
                    headers {
                        appendIfNameAbsent(
                            HttpHeaders.ContentType,
                            ContentType.Application.Json.toString(),
                        )
                    }
                }

                NetworkSource.ARTICLE -> {
                    url(NetworkSource.Article.BASE_URL)
                    headers {
                        appendIfNameAbsent(
                            HttpHeaders.ContentType,
                            ContentType.Application.Json.toString(),
                        )
                    }
                }

                NetworkSource.QURAN -> {
                    url(NetworkSource.Quran.BASE_URL)
                    headers {
                        appendIfNameAbsent(
                            HttpHeaders.ContentType,
                            ContentType.Application.Json.toString(),
                        )
                    }
                }

                NetworkSource.SUPABASE -> {
                    url(NetworkSource.Supabase.BASE_URL)
                    headers {
                        appendIfNameAbsent(
                            HttpHeaders.ContentType,
                            ContentType.Application.Json.toString(),
                        )
                        appendIfNameAbsent(
                            NetworkSource.Supabase.PARAM_API_KEY,
                            NetworkSource.Supabase.SUPABASE_API_KEY,
                        )
                    }
                }

                NetworkSource.YOUTUBE -> {
                    url(NetworkSource.Youtube.BASE_URL)
                    headers {
                        appendIfNameAbsent(
                            HttpHeaders.ContentType,
                            ContentType.Application.Json.toString(),
                        )
                    }
                }
            }
        }

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                },
            )
        }
        install(Logging) {
            level = LogLevel.ALL
            logger =
                object : Logger {
                    override fun log(message: String) {
                        Napier.d(tag = "API LOG", message = message)
                    }
                }
        }.also {
            Napier.base(DebugAntilog())
        }
    }
