package feature.prayertime.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.prayertime.service.entity.GuidanceRoom

@Dao
interface GuidanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(guidanceRoom: GuidanceRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(guidanceRoomList: List<GuidanceRoom>)

    @Update
    suspend fun update(guidanceRoom: GuidanceRoom)

    @Update
    suspend fun update(guidanceRoomList: List<GuidanceRoom>)

    @Delete
    suspend fun delete(guidanceRoom: GuidanceRoom)

    @Delete
    suspend fun delete(guidanceRoomList: List<GuidanceRoom>)

    @Query("DELETE FROM GuidanceRoom")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM GuidanceRoom")
    suspend fun count(): Int

    @Query("SELECT * FROM GuidanceRoom")
    suspend fun getAll(): List<GuidanceRoom>

    @Query("SELECT * FROM GuidanceRoom WHERE type in (:types)")
    suspend fun loadAllByType(types: List<String>): List<GuidanceRoom>
}
