package data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import feature.conversation.service.entity.ConversationRealm
import feature.conversation.service.source.local.ConversationDao
import feature.dhikr.service.entity.AsmaulHusnaRealm
import feature.dhikr.service.entity.DhikrRealm
import feature.dhikr.service.entity.DuaCategoryRealm
import feature.dhikr.service.entity.DuaRealm
import feature.dhikr.service.source.local.AsmaulHusnaDao
import feature.dhikr.service.source.local.DhikrDao
import feature.dhikr.service.source.local.DuaCategoryDao
import feature.dhikr.service.source.local.DuaDao
import feature.home.service.entity.HomeTemplateRealm
import feature.home.service.resource.local.HomeTemplateDao
import feature.other.service.entity.AppSettingRealm
import feature.other.service.source.local.AppSettingDao
import feature.prayertime.service.entity.GuidanceRealm
import feature.prayertime.service.entity.PrayerTimeRealm
import feature.prayertime.service.source.local.GuidanceDao
import feature.prayertime.service.source.local.PrayerTimeDao
import feature.quran.service.entity.ChapterRealm
import feature.quran.service.entity.JuzRealm
import feature.quran.service.entity.LastReadRealm
import feature.quran.service.entity.PageRealm
import feature.quran.service.entity.VerseFavoriteRealm
import feature.quran.service.entity.VerseRealm
import feature.quran.service.source.local.ChapterDao
import feature.quran.service.source.local.JuzDao
import feature.quran.service.source.local.LastReadDao
import feature.quran.service.source.local.PageDao
import feature.quran.service.source.local.VerseDao
import feature.quran.service.source.local.VerseFavoriteDao
import feature.study.service.entity.NoteRealm
import feature.study.service.source.local.NoteDao

@Database(
    entities = [
        AppSettingRealm::class,
        AsmaulHusnaRealm::class,
        ChapterRealm::class,
        ConversationRealm::class,
        DhikrRealm::class,
        DuaCategoryRealm::class,
        DuaRealm::class,
        GuidanceRealm::class,
        HomeTemplateRealm::class,
        JuzRealm::class,
        LastReadRealm::class,
        NoteRealm::class,
        PageRealm::class,
        PrayerTimeRealm::class,
        VerseFavoriteRealm::class,
        VerseRealm::class,
    ],
    version = 1,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appSettingDao(): AppSettingDao

    abstract fun asmaulHusnaDao(): AsmaulHusnaDao

    abstract fun chapterDao(): ChapterDao

    abstract fun conversationDao(): ConversationDao

    abstract fun dhikrDao(): DhikrDao

    abstract fun duaDao(): DuaDao

    abstract fun duaCategoryDao(): DuaCategoryDao

    abstract fun guidanceDao(): GuidanceDao

    abstract fun homeTemplateDao(): HomeTemplateDao

    abstract fun juzDao(): JuzDao

    abstract fun lastReadDao(): LastReadDao

    abstract fun noteDao(): NoteDao

    abstract fun pageDao(): PageDao

    abstract fun prayerTimeDao(): PrayerTimeDao

    abstract fun verseDao(): VerseDao

    abstract fun verseFavoriteDao(): VerseFavoriteDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
