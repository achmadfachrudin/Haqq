package feature.quran.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.quran.service.entity.ChapterRealm

@Dao
interface ChapterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chapterRealm: ChapterRealm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chapterRealmList: List<ChapterRealm>)

    @Update
    suspend fun update(chapterRealm: ChapterRealm)

    @Update
    suspend fun update(chapterRealmList: List<ChapterRealm>)

    @Delete
    suspend fun delete(chapterRealm: ChapterRealm)

    @Delete
    suspend fun delete(chapterRealmList: List<ChapterRealm>)

    @Query("DELETE FROM ChapterRealm")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM ChapterRealm")
    suspend fun count(): Int

    @Query("SELECT * FROM ChapterRealm")
    suspend fun getAll(): List<ChapterRealm>

    @Query("SELECT * FROM ChapterRealm WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<ChapterRealm>

    @Query("SELECT * FROM ChapterRealm WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        ChapterRealm,
    >
}
