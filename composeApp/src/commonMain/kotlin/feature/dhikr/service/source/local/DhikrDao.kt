package feature.dhikr.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.dhikr.service.entity.DhikrRealm

@Dao
interface DhikrDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dhikrRealm: DhikrRealm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dhikrRealmList: List<DhikrRealm>)

    @Update
    suspend fun update(dhikrRealm: DhikrRealm)

    @Update
    suspend fun update(dhikrRealmList: List<DhikrRealm>)

    @Delete
    suspend fun delete(dhikrRealm: DhikrRealm)

    @Delete
    suspend fun delete(dhikrRealmList: List<DhikrRealm>)

    @Query("DELETE FROM DhikrRealm")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM DhikrRealm")
    suspend fun count(): Int

    @Query("SELECT * FROM DhikrRealm")
    suspend fun getAll(): List<DhikrRealm>

    @Query("SELECT * FROM DhikrRealm WHERE type in (:types)")
    suspend fun loadAllByType(types: List<String>): List<DhikrRealm>

    @Query("SELECT * FROM DhikrRealm WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        DhikrRealm,
    >
}
