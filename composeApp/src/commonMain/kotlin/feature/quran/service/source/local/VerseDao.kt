package feature.quran.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.quran.service.entity.VerseRoom

@Dao
interface VerseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(verseRoom: VerseRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(verseRoomList: List<VerseRoom>)

    @Update
    suspend fun update(verseRoom: VerseRoom)

    @Update
    suspend fun update(verseRoomList: List<VerseRoom>)

    @Delete
    suspend fun delete(verseRoom: VerseRoom)

    @Delete
    suspend fun delete(verseRoomList: List<VerseRoom>)

    @Query("DELETE FROM VerseRoom")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM VerseRoom")
    suspend fun count(): Int

    @Query("SELECT * FROM VerseRoom")
    suspend fun getAll(): List<VerseRoom>

    @Query("SELECT * FROM VerseRoom WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<VerseRoom>

    @Query("SELECT * FROM VerseRoom WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        VerseRoom,
    >
}
