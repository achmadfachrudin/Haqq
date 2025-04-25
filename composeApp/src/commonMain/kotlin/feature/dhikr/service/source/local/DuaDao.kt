package feature.dhikr.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.dhikr.service.entity.DuaRealm

@Dao
interface DuaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(duaRealm: DuaRealm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(duaRealmList: List<DuaRealm>)

    @Update
    suspend fun update(duaRealm: DuaRealm)

    @Update
    suspend fun update(duaRealmList: List<DuaRealm>)

    @Delete
    suspend fun delete(duaRealm: DuaRealm)

    @Delete
    suspend fun delete(duaRealmList: List<DuaRealm>)

    @Query("DELETE FROM DuaRealm")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM DuaRealm")
    suspend fun count(): Int

    @Query("SELECT * FROM DuaRealm")
    suspend fun getAll(): List<DuaRealm>

    @Query("SELECT * FROM DuaRealm WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<DuaRealm>

    @Query("SELECT * FROM DuaRealm WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        DuaRealm,
    >
}
