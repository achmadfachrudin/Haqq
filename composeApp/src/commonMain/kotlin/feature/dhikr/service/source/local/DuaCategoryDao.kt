package feature.dhikr.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.dhikr.service.entity.DuaCategoryRoom

@Dao
interface DuaCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(duaCategoryRoom: DuaCategoryRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(duaCategoryRoomList: List<DuaCategoryRoom>)

    @Update
    suspend fun update(duaCategoryRoom: DuaCategoryRoom)

    @Update
    suspend fun update(duaCategoryRoomList: List<DuaCategoryRoom>)

    @Delete
    suspend fun delete(duaCategoryRoom: DuaCategoryRoom)

    @Delete
    suspend fun delete(duaCategoryRoomList: List<DuaCategoryRoom>)

    @Query("DELETE FROM DuaCategoryRoom")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM DuaCategoryRoom")
    suspend fun count(): Int

    @Query("SELECT * FROM DuaCategoryRoom")
    suspend fun getAll(): List<DuaCategoryRoom>

    @Query("SELECT * FROM DuaCategoryRoom WHERE tag in (:tags)")
    suspend fun loadAllByTag(tags: List<String>): List<DuaCategoryRoom>
}
