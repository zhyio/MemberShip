package com.membership.app.data.repository

import com.membership.app.data.local.dao.MemberDao
import com.membership.app.data.local.entity.MemberEntity
import com.membership.app.domain.model.Member
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

enum class SortOrder {
    UPDATE_TIME, CREATE_TIME, NAME, AGE
}

@Singleton
class MemberRepository @Inject constructor(
    private val memberDao: MemberDao
) {
    fun getAllMembers(sortOrder: SortOrder = SortOrder.UPDATE_TIME): Flow<List<Member>> {
        val flow = when (sortOrder) {
            SortOrder.UPDATE_TIME -> memberDao.getAllMembers()
            SortOrder.CREATE_TIME -> memberDao.getMembersByCreateTime()
            SortOrder.NAME -> memberDao.getMembersByName()
            SortOrder.AGE -> memberDao.getMembersByAge()
        }
        return flow.map { entities ->
            entities.map { it.toMember() }
        }
    }

    suspend fun getMemberById(id: Long): Member? {
        return memberDao.getMemberById(id)?.toMember()
    }

    fun searchMembers(query: String): Flow<List<Member>> {
        return memberDao.searchMembers(query).map { entities ->
            entities.map { it.toMember() }
        }
    }

    fun getMembersByGender(gender: Int): Flow<List<Member>> {
        return memberDao.getMembersByGender(gender).map { entities ->
            entities.map { it.toMember() }
        }
    }

    fun getMembersByMaritalStatus(status: Int): Flow<List<Member>> {
        return memberDao.getMembersByMaritalStatus(status).map { entities ->
            entities.map { it.toMember() }
        }
    }

    fun getFavoriteMembers(): Flow<List<Member>> {
        return memberDao.getFavoriteMembers().map { entities ->
            entities.map { it.toMember() }
        }
    }

    suspend fun insertMember(member: Member): Long {
        return memberDao.insertMember(MemberEntity.fromMember(member))
    }

    suspend fun updateMember(member: Member) {
        memberDao.updateMember(MemberEntity.fromMember(member))
    }

    suspend fun deleteMember(member: Member) {
        memberDao.deleteMember(MemberEntity.fromMember(member))
    }

    suspend fun deleteMemberById(id: Long) {
        memberDao.deleteMemberById(id)
    }

    suspend fun getMemberCount(): Int = memberDao.getMemberCount()
    suspend fun getMaleCount(): Int = memberDao.getMaleCount()
    suspend fun getFemaleCount(): Int = memberDao.getFemaleCount()

    fun observeMemberCount(): Flow<Int> = memberDao.observeMemberCount()
    fun observeMaleCount(): Flow<Int> = memberDao.observeMaleCount()
    fun observeFemaleCount(): Flow<Int> = memberDao.observeFemaleCount()

    suspend fun toggleFavorite(member: Member) {
        val updated = member.copy(
            isFavorite = !member.isFavorite,
            updatedAt = java.util.Date()
        )
        updateMember(updated)
    }
}
