package feature.quran.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.quran.service.entity.JuzRoom

@Dao
interface JuzDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(juzRoom: JuzRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(juzRoomList: List<JuzRoom>)

    @Update
    suspend fun update(juzRoom: JuzRoom)

    @Update
    suspend fun update(juzRoomList: List<JuzRoom>)

    @Delete
    suspend fun delete(juzRoom: JuzRoom)

    @Delete
    suspend fun delete(juzRoomList: List<JuzRoom>)

    @Query("DELETE FROM JuzRoom")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM JuzRoom")
    suspend fun count(): Int

    @Query("SELECT * FROM JuzRoom")
    suspend fun getAll(): List<JuzRoom>

    @Query("SELECT * FROM JuzRoom WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<JuzRoom>

    @Query("SELECT * FROM JuzRoom WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        JuzRoom,
    >
}
