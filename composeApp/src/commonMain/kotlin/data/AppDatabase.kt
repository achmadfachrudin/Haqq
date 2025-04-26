package data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import feature.conversation.service.entity.ConversationRoom
import feature.conversation.service.source.local.ConversationDao
import feature.dhikr.service.entity.AsmaulHusnaRoom
import feature.dhikr.service.entity.DhikrRoom
import feature.dhikr.service.entity.DuaCategoryRoom
import feature.dhikr.service.entity.DuaRoom
import feature.dhikr.service.source.local.AsmaulHusnaDao
import feature.dhikr.service.source.local.DhikrDao
import feature.dhikr.service.source.local.DuaCategoryDao
import feature.dhikr.service.source.local.DuaDao
import feature.home.service.entity.HomeTemplateRoom
import feature.home.service.resource.local.HomeTemplateDao
import feature.other.service.entity.AppSettingRoom
import feature.other.service.source.local.AppSettingDao
import feature.prayertime.service.entity.GuidanceRoom
import feature.prayertime.service.entity.PrayerTimeRoom
import feature.prayertime.service.source.local.GuidanceDao
import feature.prayertime.service.source.local.PrayerTimeDao
import feature.quran.service.entity.ChapterRoom
import feature.quran.service.entity.JuzRoom
import feature.quran.service.entity.LastReadRoom
import feature.quran.service.entity.PageRoom
import feature.quran.service.entity.VerseFavoriteRoom
import feature.quran.service.entity.VerseRoom
import feature.quran.service.source.local.ChapterDao
import feature.quran.service.source.local.JuzDao
import feature.quran.service.source.local.LastReadDao
import feature.quran.service.source.local.PageDao
import feature.quran.service.source.local.VerseDao
import feature.quran.service.source.local.VerseFavoriteDao
import feature.study.service.entity.NoteRoom
import feature.study.service.source.local.NoteDao

@Database(
    entities = [
        AppSettingRoom::class,
        AsmaulHusnaRoom::class,
        ChapterRoom::class,
        ConversationRoom::class,
        DhikrRoom::class,
        DuaCategoryRoom::class,
        DuaRoom::class,
        GuidanceRoom::class,
        HomeTemplateRoom::class,
        JuzRoom::class,
        LastReadRoom::class,
        NoteRoom::class,
        PageRoom::class,
        PrayerTimeRoom::class,
        VerseFavoriteRoom::class,
        VerseRoom::class,
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
internal expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
