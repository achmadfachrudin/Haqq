package feature.home.service.resource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.home.service.entity.HomeTemplateRealm

@Dao
interface HomeTemplateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(homeTemplateRealm: HomeTemplateRealm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(homeTemplateRealmList: List<HomeTemplateRealm>)

    @Update
    suspend fun update(homeTemplateRealm: HomeTemplateRealm)

    @Update
    suspend fun update(homeTemplateRealmList: List<HomeTemplateRealm>)

    @Delete
    suspend fun delete(homeTemplateRealm: HomeTemplateRealm)

    @Delete
    suspend fun delete(homeTemplateRealmList: List<HomeTemplateRealm>)

    @Query("DELETE FROM HomeTemplateRealm")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM HomeTemplateRealm")
    suspend fun count(): Int

    @Query("SELECT * FROM HomeTemplateRealm")
    suspend fun getAll(): List<HomeTemplateRealm>

    @Query("SELECT * FROM HomeTemplateRealm WHERE position in (:positions)")
    suspend fun loadAllByPosition(positions: List<Int>): List<HomeTemplateRealm>
}
