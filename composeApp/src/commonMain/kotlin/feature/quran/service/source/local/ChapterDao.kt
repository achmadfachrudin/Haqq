package feature.quran.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.quran.service.entity.ChapterRoom

@Dao
interface ChapterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chapterRoom: ChapterRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chapterRoomList: List<ChapterRoom>)

    @Update
    suspend fun update(chapterRoom: ChapterRoom)

    @Update
    suspend fun update(chapterRoomList: List<ChapterRoom>)

    @Delete
    suspend fun delete(chapterRoom: ChapterRoom)

    @Delete
    suspend fun delete(chapterRoomList: List<ChapterRoom>)

    @Query("DELETE FROM ChapterRoom")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM ChapterRoom")
    suspend fun count(): Int

    @Query("SELECT * FROM ChapterRoom")
    suspend fun getAll(): List<ChapterRoom>

    @Query("SELECT * FROM ChapterRoom WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<ChapterRoom>

    @Query("SELECT * FROM ChapterRoom WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        ChapterRoom,
    >
}
