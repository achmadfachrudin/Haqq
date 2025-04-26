package feature.quran.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.quran.service.entity.VerseFavoriteRoom

@Dao
interface VerseFavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(verseFavoriteRoom: VerseFavoriteRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(verseFavoriteRoomList: List<VerseFavoriteRoom>)

    @Update
    suspend fun update(verseFavoriteRoom: VerseFavoriteRoom)

    @Update
    suspend fun update(verseFavoriteRoomList: List<VerseFavoriteRoom>)

    @Delete
    suspend fun delete(verseFavoriteRoom: VerseFavoriteRoom)

    @Delete
    suspend fun delete(verseFavoriteRoomList: List<VerseFavoriteRoom>)

    @Query("DELETE FROM VerseFavoriteRoom")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM VerseFavoriteRoom")
    suspend fun count(): Int

    @Query("SELECT * FROM VerseFavoriteRoom")
    suspend fun getAll(): List<VerseFavoriteRoom>

    @Query("SELECT * FROM VerseFavoriteRoom WHERE verseId in (:verseIds)")
    suspend fun loadAllByVerseId(verseIds: List<Int>): List<VerseFavoriteRoom>
}
