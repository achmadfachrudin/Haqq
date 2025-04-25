import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import core.data.NetworkSource.Room.ROOM_DATABASE
import data.AppDatabase
import kotlinx.coroutines.Dispatchers

fun createRoomDatabase(context: Context): AppDatabase {
    val dbFile = context.getDatabasePath(ROOM_DATABASE)
    return Room
        .databaseBuilder<AppDatabase>(context, dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

// fun dataStore(context: Context): DataStore<Preferences> =
//    createDataStore(
//        producePath = { context.filesDir.resolve("ROOM_PREFERENCES").absolutePath }
//    )
