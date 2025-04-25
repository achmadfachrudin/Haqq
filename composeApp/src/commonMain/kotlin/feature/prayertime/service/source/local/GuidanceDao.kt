package feature.prayertime.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.prayertime.service.entity.GuidanceRealm

@Dao
interface GuidanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(guidanceRealm: GuidanceRealm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(guidanceRealmList: List<GuidanceRealm>)

    @Update
    suspend fun update(guidanceRealm: GuidanceRealm)

    @Update
    suspend fun update(guidanceRealmList: List<GuidanceRealm>)

    @Delete
    suspend fun delete(guidanceRealm: GuidanceRealm)

    @Delete
    suspend fun delete(guidanceRealmList: List<GuidanceRealm>)

    @Query("DELETE FROM GuidanceRealm")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM GuidanceRealm")
    suspend fun count(): Int

    @Query("SELECT * FROM GuidanceRealm")
    suspend fun getAll(): List<GuidanceRealm>

    @Query("SELECT * FROM GuidanceRealm WHERE type in (:types)")
    suspend fun loadAllByType(types: List<String>): List<GuidanceRealm>
}
