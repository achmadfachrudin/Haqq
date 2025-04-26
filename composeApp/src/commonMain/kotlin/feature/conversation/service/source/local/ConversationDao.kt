package feature.conversation.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.conversation.service.entity.ConversationRoom

@Dao
interface ConversationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(conversationRoom: ConversationRoom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(conversationRoomList: List<ConversationRoom>)

    @Update
    suspend fun update(conversationRoom: ConversationRoom)

    @Update
    suspend fun update(conversationRoomList: List<ConversationRoom>)

    @Delete
    suspend fun delete(conversationRoom: ConversationRoom)

    @Delete
    suspend fun delete(conversationRoomList: List<ConversationRoom>)

    @Query("DELETE FROM ConversationRoom")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM ConversationRoom")
    suspend fun count(): Int

    @Query("SELECT * FROM ConversationRoom")
    suspend fun getAll(): List<ConversationRoom>

    @Query("SELECT * FROM ConversationRoom WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<ConversationRoom>

    @Query("SELECT * FROM ConversationRoom WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        ConversationRoom,
    >
}
