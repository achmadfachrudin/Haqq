package feature.quran.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.quran.service.entity.VerseRealm

@Dao
interface VerseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(verseRealm: VerseRealm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(verseRealmList: List<VerseRealm>)

    @Update
    suspend fun update(verseRealm: VerseRealm)

    @Update
    suspend fun update(verseRealmList: List<VerseRealm>)

    @Delete
    suspend fun delete(verseRealm: VerseRealm)

    @Delete
    suspend fun delete(verseRealmList: List<VerseRealm>)

    @Query("DELETE FROM VerseRealm")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM VerseRealm")
    suspend fun count(): Int

    @Query("SELECT * FROM VerseRealm")
    suspend fun getAll(): List<VerseRealm>

    @Query("SELECT * FROM VerseRealm WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<VerseRealm>

    @Query("SELECT * FROM VerseRealm WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        VerseRealm,
    >
}
