package com.membership.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.membership.app.data.local.dao.MemberDao
import com.membership.app.data.local.entity.MemberEntity

@Database(
    entities = [MemberEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MembershipDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao
}
