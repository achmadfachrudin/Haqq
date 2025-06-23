package feature.quran.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.quran.service.entity.LastReadRoom

@Dao
interface LastReadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lastReadRoom: LastReadRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lastReadRoomList: List<LastReadRoom>)

    @Update
    suspend fun update(lastReadRoom: LastReadRoom)

    @Update
    suspend fun update(lastReadRoomList: List<LastReadRoom>)

    @Delete
    suspend fun delete(lastReadRoom: LastReadRoom)

    @Delete
    suspend fun delete(lastReadRoomList: List<LastReadRoom>)

    @Query("DELETE FROM LastReadRoom")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM LastReadRoom")
    suspend fun count(): Int

    @Query("SELECT * FROM LastReadRoom")
    suspend fun getAll(): List<LastReadRoom>

    @Query("SELECT * FROM LastReadRoom WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<LastReadRoom>

    @Query("SELECT * FROM LastReadRoom WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        LastReadRoom,
    >
}
