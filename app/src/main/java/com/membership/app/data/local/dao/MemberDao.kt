package com.membership.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.membership.app.data.local.entity.MemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    @Query("SELECT * FROM members ORDER BY updated_at DESC")
    fun getAllMembers(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members ORDER BY created_at DESC")
    fun getMembersByCreateTime(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members ORDER BY name ASC")
    fun getMembersByName(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members ORDER BY birth_date ASC")
    fun getMembersByAge(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE id = :id")
    suspend fun getMemberById(id: Long): MemberEntity?

    @Query("SELECT * FROM members WHERE name LIKE '%' || :query || '%' OR city LIKE '%' || :query || '%' OR occupation LIKE '%' || :query || '%' ORDER BY updated_at DESC")
    fun searchMembers(query: String): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE gender = :gender ORDER BY updated_at DESC")
    fun getMembersByGender(gender: Int): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE marital_status = :status ORDER BY updated_at DESC")
    fun getMembersByMaritalStatus(status: Int): Flow<List<MemberEntity>>

    @Query("SELECT * FROM members WHERE is_favorite = 1 ORDER BY updated_at DESC")
    fun getFavoriteMembers(): Flow<List<MemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: MemberEntity): Long

    @Update
    suspend fun updateMember(member: MemberEntity)

    @Delete
    suspend fun deleteMember(member: MemberEntity)

    @Query("DELETE FROM members WHERE id = :id")
    suspend fun deleteMemberById(id: Long)

    @Query("SELECT COUNT(*) FROM members")
    suspend fun getMemberCount(): Int

    @Query("SELECT COUNT(*) FROM members WHERE gender = 1")
    suspend fun getMaleCount(): Int

    @Query("SELECT COUNT(*) FROM members WHERE gender = 0")
    suspend fun getFemaleCount(): Int

    @Query("SELECT COUNT(*) FROM members")
    fun observeMemberCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM members WHERE gender = 1")
    fun observeMaleCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM members WHERE gender = 0")
    fun observeFemaleCount(): Flow<Int>
}
