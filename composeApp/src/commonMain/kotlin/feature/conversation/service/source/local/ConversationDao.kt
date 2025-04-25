package feature.conversation.service.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import feature.conversation.service.entity.ConversationRealm

@Dao
interface ConversationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(conversationRealm: ConversationRealm)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(conversationRealmList: List<ConversationRealm>)

    @Update
    suspend fun update(conversationRealm: ConversationRealm)

    @Update
    suspend fun update(conversationRealmList: List<ConversationRealm>)

    @Delete
    suspend fun delete(conversationRealm: ConversationRealm)

    @Delete
    suspend fun delete(conversationRealmList: List<ConversationRealm>)

    @Query("DELETE FROM ConversationRealm")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) as count FROM ConversationRealm")
    suspend fun count(): Int

    @Query("SELECT * FROM ConversationRealm")
    suspend fun getAll(): List<ConversationRealm>

    @Query("SELECT * FROM ConversationRealm WHERE id in (:ids)")
    suspend fun loadAllById(ids: List<Int>): List<ConversationRealm>

    @Query("SELECT * FROM ConversationRealm WHERE id in (:ids)")
    suspend fun loadMappedById(ids: List<Int>): Map<
        @MapColumn(columnName = "id")
        Long,
        ConversationRealm,
    >
}
