package feature.quran.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.quran.service.entity.PageRoom

@Dao
interface PageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pageRoom: PageRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pageRoomList: List<PageRoom>)

    @Update
    suspend fun update(pageRoom: PageRoom)

    @Update
    suspend fun update(pageRoomList: List<PageRoom>)

    @Delete
    suspend fun delete(pageRoom: PageRoom)

    @Delete
    suspend fun delete(pageRoomList: List<PageRoom>)

    @Query("DELETE FROM PageRoom")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM PageRoom")
    suspend fun count(): Int

    @Query("SELECT * FROM PageRoom")
    suspend fun getAll(): List<PageRoom>

    @Query("SELECT * FROM PageRoom WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<PageRoom>

    @Query("SELECT * FROM PageRoom WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        PageRoom,
    >
}
