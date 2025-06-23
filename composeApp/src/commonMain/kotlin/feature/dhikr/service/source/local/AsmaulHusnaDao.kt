package feature.dhikr.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.dhikr.service.entity.AsmaulHusnaRoom

@Dao
interface AsmaulHusnaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asmaulHusnaRoom: AsmaulHusnaRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asmaulHusnaRoomList: List<AsmaulHusnaRoom>)

    @Update
    suspend fun update(asmaulHusnaRoom: AsmaulHusnaRoom)

    @Update
    suspend fun update(asmaulHusnaRoomList: List<AsmaulHusnaRoom>)

    @Delete
    suspend fun delete(asmaulHusnaRoom: AsmaulHusnaRoom)

    @Delete
    suspend fun delete(asmaulHusnaRoomList: List<AsmaulHusnaRoom>)

    @Query("DELETE FROM AsmaulHusnaRoom")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM AsmaulHusnaRoom")
    suspend fun count(): Int

    @Query("SELECT * FROM AsmaulHusnaRoom")
    suspend fun getAll(): List<AsmaulHusnaRoom>

    @Query("SELECT * FROM AsmaulHusnaRoom WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<AsmaulHusnaRoom>

    @Query("SELECT * FROM AsmaulHusnaRoom WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        AsmaulHusnaRoom,
    >
}
