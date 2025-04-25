package feature.study.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.study.service.entity.NoteRealm

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteRealm: NoteRealm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteRealmList: List<NoteRealm>)

    @Update
    suspend fun update(noteRealm: NoteRealm)

    @Update
    suspend fun update(noteRealmList: List<NoteRealm>)

    @Delete
    suspend fun delete(noteRealm: NoteRealm)

    @Delete
    suspend fun delete(noteRealmList: List<NoteRealm>)

    @Query("DELETE FROM NoteRealm")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM NoteRealm")
    suspend fun count(): Int

    @Query("SELECT * FROM NoteRealm")
    suspend fun getAll(): List<NoteRealm>

    @Query("SELECT * FROM NoteRealm WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<NoteRealm>

    @Query("SELECT * FROM NoteRealm WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        NoteRealm,
    >
}
