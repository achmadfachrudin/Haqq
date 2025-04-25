package feature.dhikr.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.dhikr.service.entity.AsmaulHusnaRealm

@Dao
interface AsmaulHusnaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asmaulHusnaRealm: AsmaulHusnaRealm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asmaulHusnaRealmList: List<AsmaulHusnaRealm>)

    @Update
    suspend fun update(asmaulHusnaRealm: AsmaulHusnaRealm)

    @Update
    suspend fun update(asmaulHusnaRealmList: List<AsmaulHusnaRealm>)

    @Delete
    suspend fun delete(asmaulHusnaRealm: AsmaulHusnaRealm)

    @Delete
    suspend fun delete(asmaulHusnaRealmList: List<AsmaulHusnaRealm>)

    @Query("DELETE FROM AsmaulHusnaRealm")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM AsmaulHusnaRealm")
    suspend fun count(): Int

    @Query("SELECT * FROM AsmaulHusnaRealm")
    suspend fun getAll(): List<AsmaulHusnaRealm>

    @Query("SELECT * FROM AsmaulHusnaRealm WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<AsmaulHusnaRealm>

    @Query("SELECT * FROM AsmaulHusnaRealm WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        AsmaulHusnaRealm,
    >
}
