package feature.other.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.other.service.entity.AppSettingRealm

@Dao
interface AppSettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appSettingRealm: AppSettingRealm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appSettingRealmList: List<AppSettingRealm>)

    @Update
    suspend fun update(appSettingRealm: AppSettingRealm)

    @Update
    suspend fun update(appSettingRealmList: List<AppSettingRealm>)

    @Delete
    suspend fun delete(appSettingRealm: AppSettingRealm)

    @Delete
    suspend fun delete(appSettingRealmList: List<AppSettingRealm>)

    @Query("DELETE FROM AppSettingRealm")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM AppSettingRealm")
    suspend fun count(): Int

    @Query("SELECT * FROM AppSettingRealm")
    suspend fun getAll(): List<AppSettingRealm>

    @Query("SELECT * FROM AppSettingRealm WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<AppSettingRealm>

    @Query("SELECT * FROM AppSettingRealm WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        AppSettingRealm,
    >
}
