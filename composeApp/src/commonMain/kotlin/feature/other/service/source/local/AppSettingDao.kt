package feature.other.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.other.service.entity.AppSettingRoom

@Dao
interface AppSettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appSettingRoom: AppSettingRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appSettingRoomList: List<AppSettingRoom>)

    @Update
    suspend fun update(appSettingRoom: AppSettingRoom)

    @Update
    suspend fun update(appSettingRoomList: List<AppSettingRoom>)

    @Delete
    suspend fun delete(appSettingRoom: AppSettingRoom)

    @Delete
    suspend fun delete(appSettingRoomList: List<AppSettingRoom>)

    @Query("DELETE FROM AppSettingRoom")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM AppSettingRoom")
    suspend fun count(): Int

    @Query("SELECT * FROM AppSettingRoom")
    suspend fun getAll(): List<AppSettingRoom>

    @Query("SELECT * FROM AppSettingRoom WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<AppSettingRoom>

    @Query("SELECT * FROM AppSettingRoom WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        AppSettingRoom,
    >
}
