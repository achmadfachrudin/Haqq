package feature.dhikr.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.dhikr.service.entity.DhikrRoom

@Dao
interface DhikrDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dhikrRoom: DhikrRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dhikrRoomList: List<DhikrRoom>)

    @Update
    suspend fun update(dhikrRoom: DhikrRoom)

    @Update
    suspend fun update(dhikrRoomList: List<DhikrRoom>)

    @Delete
    suspend fun delete(dhikrRoom: DhikrRoom)

    @Delete
    suspend fun delete(dhikrRoomList: List<DhikrRoom>)

    @Query("DELETE FROM DhikrRoom")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM DhikrRoom")
    suspend fun count(): Int

    @Query("SELECT * FROM DhikrRoom")
    suspend fun getAll(): List<DhikrRoom>

    @Query("SELECT * FROM DhikrRoom WHERE type in (:types)")
    suspend fun loadAllByType(types: List<String>): List<DhikrRoom>

    @Query("SELECT * FROM DhikrRoom WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        DhikrRoom,
    >
}
