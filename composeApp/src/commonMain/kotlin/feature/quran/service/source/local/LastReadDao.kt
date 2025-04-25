package feature.quran.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.quran.service.entity.LastReadRealm

@Dao
interface LastReadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lastReadRealm: LastReadRealm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lastReadRealmList: List<LastReadRealm>)

    @Update
    suspend fun update(lastReadRealm: LastReadRealm)

    @Update
    suspend fun update(lastReadRealmList: List<LastReadRealm>)

    @Delete
    suspend fun delete(lastReadRealm: LastReadRealm)

    @Delete
    suspend fun delete(lastReadRealmList: List<LastReadRealm>)

    @Query("DELETE FROM LastReadRealm")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM LastReadRealm")
    suspend fun count(): Int

    @Query("SELECT * FROM LastReadRealm")
    suspend fun getAll(): List<LastReadRealm>

    @Query("SELECT * FROM LastReadRealm WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<LastReadRealm>

    @Query("SELECT * FROM LastReadRealm WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        LastReadRealm,
    >
}
