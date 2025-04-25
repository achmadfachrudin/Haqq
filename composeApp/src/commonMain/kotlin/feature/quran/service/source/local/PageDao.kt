package feature.quran.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.quran.service.entity.PageRealm

@Dao
interface PageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pageRealm: PageRealm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pageRealmList: List<PageRealm>)

    @Update
    suspend fun update(pageRealm: PageRealm)

    @Update
    suspend fun update(pageRealmList: List<PageRealm>)

    @Delete
    suspend fun delete(pageRealm: PageRealm)

    @Delete
    suspend fun delete(pageRealmList: List<PageRealm>)

    @Query("DELETE FROM PageRealm")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM PageRealm")
    suspend fun count(): Int

    @Query("SELECT * FROM PageRealm")
    suspend fun getAll(): List<PageRealm>

    @Query("SELECT * FROM PageRealm WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<PageRealm>

    @Query("SELECT * FROM PageRealm WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        PageRealm,
    >
}
