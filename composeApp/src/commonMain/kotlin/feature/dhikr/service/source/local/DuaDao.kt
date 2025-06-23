package feature.dhikr.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.dhikr.service.entity.DuaRoom

@Dao
interface DuaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(duaRoom: DuaRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(duaRoomList: List<DuaRoom>)

    @Update
    suspend fun update(duaRoom: DuaRoom)

    @Update
    suspend fun update(duaRoomList: List<DuaRoom>)

    @Delete
    suspend fun delete(duaRoom: DuaRoom)

    @Delete
    suspend fun delete(duaRoomList: List<DuaRoom>)

    @Query("DELETE FROM DuaRoom")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM DuaRoom")
    suspend fun count(): Int

    @Query("SELECT * FROM DuaRoom")
    suspend fun getAll(): List<DuaRoom>

    @Query("SELECT * FROM DuaRoom WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<DuaRoom>

    @Query("SELECT * FROM DuaRoom WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        DuaRoom,
    >
}
