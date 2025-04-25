package feature.quran.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.quran.service.entity.VerseFavoriteRealm

@Dao
interface VerseFavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(verseFavoriteRealm: VerseFavoriteRealm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(verseFavoriteRealmList: List<VerseFavoriteRealm>)

    @Update
    suspend fun update(verseFavoriteRealm: VerseFavoriteRealm)

    @Update
    suspend fun update(verseFavoriteRealmList: List<VerseFavoriteRealm>)

    @Delete
    suspend fun delete(verseFavoriteRealm: VerseFavoriteRealm)

    @Delete
    suspend fun delete(verseFavoriteRealmList: List<VerseFavoriteRealm>)

    @Query("DELETE FROM VerseFavoriteRealm")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM VerseFavoriteRealm")
    suspend fun count(): Int

    @Query("SELECT * FROM VerseFavoriteRealm")
    suspend fun getAll(): List<VerseFavoriteRealm>

    @Query("SELECT * FROM VerseFavoriteRealm WHERE verseId in (:verseIds)")
    suspend fun loadAllByVerseId(verseIds: List<Int>): List<VerseFavoriteRealm>
}
