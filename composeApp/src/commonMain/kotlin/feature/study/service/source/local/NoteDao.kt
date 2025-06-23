package feature.study.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.study.service.entity.NoteRoom

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteRoom: NoteRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteRoomList: List<NoteRoom>)

    @Update
    suspend fun update(noteRoom: NoteRoom)

    @Update
    suspend fun update(noteRoomList: List<NoteRoom>)

    @Delete
    suspend fun delete(noteRoom: NoteRoom)

    @Delete
    suspend fun delete(noteRoomList: List<NoteRoom>)

    @Query("DELETE FROM NoteRoom")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM NoteRoom")
    suspend fun count(): Int

    @Query("SELECT * FROM NoteRoom")
    suspend fun getAll(): List<NoteRoom>

    @Query("SELECT * FROM NoteRoom WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<NoteRoom>

    @Query("SELECT * FROM NoteRoom WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        NoteRoom,
    >
}
