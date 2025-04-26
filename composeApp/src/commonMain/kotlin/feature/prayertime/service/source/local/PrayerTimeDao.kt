package feature.prayertime.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.prayertime.service.entity.PrayerTimeRoom

@Dao
interface PrayerTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prayerTimeRoom: PrayerTimeRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prayerTimeRoomList: List<PrayerTimeRoom>)

    @Update
    suspend fun update(prayerTimeRoom: PrayerTimeRoom)

    @Update
    suspend fun update(prayerTimeRoomList: List<PrayerTimeRoom>)

    @Delete
    suspend fun delete(prayerTimeRoom: PrayerTimeRoom)

    @Delete
    suspend fun delete(prayerTimeRoomList: List<PrayerTimeRoom>)

    @Query("DELETE FROM PrayerTimeRoom")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM PrayerTimeRoom")
    suspend fun count(): Int

    @Query("SELECT * FROM PrayerTimeRoom")
    suspend fun getAll(): List<PrayerTimeRoom>

    @Query("SELECT * FROM PrayerTimeRoom WHERE gregorianFullDate in (:gregorianFullDates)")
    suspend fun loadAllByGregorianFullDate(gregorianFullDates: List<String>): List<PrayerTimeRoom>
}
