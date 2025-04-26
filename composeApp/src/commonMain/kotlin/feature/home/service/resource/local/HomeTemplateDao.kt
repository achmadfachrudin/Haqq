package feature.home.service.resource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.home.service.entity.HomeTemplateRoom

@Dao
interface HomeTemplateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(homeTemplateRoom: HomeTemplateRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(homeTemplateRoomList: List<HomeTemplateRoom>)

    @Update
    suspend fun update(homeTemplateRoom: HomeTemplateRoom)

    @Update
    suspend fun update(homeTemplateRoomList: List<HomeTemplateRoom>)

    @Delete
    suspend fun delete(homeTemplateRoom: HomeTemplateRoom)

    @Delete
    suspend fun delete(homeTemplateRoomList: List<HomeTemplateRoom>)

    @Query("DELETE FROM HomeTemplateRoom")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM HomeTemplateRoom")
    suspend fun count(): Int

    @Query("SELECT * FROM HomeTemplateRoom")
    suspend fun getAll(): List<HomeTemplateRoom>

    @Query("SELECT * FROM HomeTemplateRoom WHERE position in (:positions)")
    suspend fun loadAllByPosition(positions: List<Int>): List<HomeTemplateRoom>
}
