package feature.prayertime.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.prayertime.service.entity.PrayerTimeRealm

@Dao
interface PrayerTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prayerTimeRealm: PrayerTimeRealm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prayerTimeRealmList: List<PrayerTimeRealm>)

    @Update
    suspend fun update(prayerTimeRealm: PrayerTimeRealm)

    @Update
    suspend fun update(prayerTimeRealmList: List<PrayerTimeRealm>)

    @Delete
    suspend fun delete(prayerTimeRealm: PrayerTimeRealm)

    @Delete
    suspend fun delete(prayerTimeRealmList: List<PrayerTimeRealm>)

    @Query("DELETE FROM PrayerTimeRealm")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM PrayerTimeRealm")
    suspend fun count(): Int

    @Query("SELECT * FROM PrayerTimeRealm")
    suspend fun getAll(): List<PrayerTimeRealm>

    @Query("SELECT * FROM PrayerTimeRealm WHERE gregorianFullDate in (:gregorianFullDates)")
    suspend fun loadAllByGregorianFullDate(gregorianFullDates: List<String>): List<PrayerTimeRealm>
}
