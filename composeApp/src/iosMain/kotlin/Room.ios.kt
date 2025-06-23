import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import core.data.NetworkSource.Room.ROOM_DATABASE
import data.AppDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

fun createRoomDatabase(): AppDatabase {
    val dbFile = "${fileDirectory()}/$ROOM_DATABASE"
    return Room
        .databaseBuilder<AppDatabase>(name = dbFile)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

@OptIn(ExperimentalForeignApi::class)
private fun fileDirectory(): String {
    val documentDirectory: NSURL? =
        NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
    return requireNotNull(documentDirectory).path!!
}

// fun dataStore(): DataStore<Preferences> = createDataStore(
//    producePath = {
//        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
//            directory = NSDocumentDirectory,
//            inDomain = NSUserDomainMask,
//            appropriateForURL = null,
//            create = false,
//            error = null,
//        )
//        requireNotNull(documentDirectory).path + "/$ROOM_PREFERENCES_PATH"
//    }
// )
