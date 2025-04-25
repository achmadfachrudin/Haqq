package feature.dhikr.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.dhikr.service.entity.DuaCategoryRealm

@Dao
interface DuaCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(duaCategoryRealm: DuaCategoryRealm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(duaCategoryRealmList: List<DuaCategoryRealm>)

    @Update
    suspend fun update(duaCategoryRealm: DuaCategoryRealm)

    @Update
    suspend fun update(duaCategoryRealmList: List<DuaCategoryRealm>)

    @Delete
    suspend fun delete(duaCategoryRealm: DuaCategoryRealm)

    @Delete
    suspend fun delete(duaCategoryRealmList: List<DuaCategoryRealm>)

    @Query("DELETE FROM DuaCategoryRealm")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM DuaCategoryRealm")
    suspend fun count(): Int

    @Query("SELECT * FROM DuaCategoryRealm")
    suspend fun getAll(): List<DuaCategoryRealm>

    @Query("SELECT * FROM DuaCategoryRealm WHERE tag in (:tags)")
    suspend fun loadAllByTag(tags: List<String>): List<DuaCategoryRealm>
}
