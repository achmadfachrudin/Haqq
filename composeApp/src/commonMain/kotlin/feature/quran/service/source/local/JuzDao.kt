package feature.quran.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.quran.service.entity.JuzRealm

@Dao
interface JuzDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(juzRealm: JuzRealm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(juzRealmList: List<JuzRealm>)

    @Update
    suspend fun update(juzRealm: JuzRealm)

    @Update
    suspend fun update(juzRealmList: List<JuzRealm>)

    @Delete
    suspend fun delete(juzRealm: JuzRealm)

    @Delete
    suspend fun delete(juzRealmList: List<JuzRealm>)

    @Query("DELETE FROM JuzRealm")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM JuzRealm")
    suspend fun count(): Int

    @Query("SELECT * FROM JuzRealm")
    suspend fun getAll(): List<JuzRealm>

    @Query("SELECT * FROM JuzRealm WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<JuzRealm>

    @Query("SELECT * FROM JuzRealm WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        JuzRealm,
    >
}
