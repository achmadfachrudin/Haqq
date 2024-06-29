package feature.quran.service.model

import kotlinx.serialization.Serializable

@Serializable
enum class ReadMode {
    BY_CHAPTER,
    BY_JUZ,
    BY_PAGE,
}
