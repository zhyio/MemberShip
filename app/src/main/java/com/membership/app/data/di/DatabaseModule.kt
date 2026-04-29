package com.membership.app.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.membership.app.data.local.MembershipDatabase
import com.membership.app.data.local.dao.MemberDao
import com.membership.app.utils.AvatarGenerator
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MembershipDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MembershipDatabase::class.java,
            "membership_database.db"
        )
            .addCallback(SeedDatabaseCallback(context))
            .build()
    }

    @Provides
    fun provideMemberDao(database: MembershipDatabase): MemberDao {
        return database.memberDao()
    }
}

private class SeedDatabaseCallback(private val appContext: Context) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        Executors.newSingleThreadExecutor().execute {
            val gson = Gson()
            val now = System.currentTimeMillis()

            data class SeedMember(
                val name: String,
                val gender: Int,
                val birthYear: Int,
                val height: Int,
                val weight: Int,
                val education: String,
                val occupation: String,
                val income: String,
                val city: String,
                val hometown: String,
                val marital: Int,
                val hasChildren: Boolean,
                val childrenCount: Int?,
                val history: String?,
                val parents: String?,
                val siblings: String?,
                val economy: String?,
                val reqMinAge: Int?,
                val reqMaxAge: Int?,
                val reqMinH: Int?,
                val reqMaxH: Int?,
                val reqEdu: String?,
                val reqCity: String?,
                val reqOther: String?
            )

            val seeds = listOf(
                SeedMember("张明", 1, 1995, 178, 72, "本科", "软件工程师", "12000-20000元", "北京", "山东济南", 0, false, null, null, "父亲公务员，母亲教师", "一个姐姐已婚", "小康", 24, 30, 160, 170, "本科及以上", "北京", "希望对方温柔体贴"),
                SeedMember("李婷", 0, 1997, 165, 50, "硕士", "产品经理", "12000-20000元", "上海", "江苏南京", 0, false, null, null, "父母经商", "独生女", "富裕", 27, 35, 170, 185, "硕士及以上", "上海", "有上进心，有责任感"),
                SeedMember("王浩然", 1, 1992, 175, 68, "博士", "大学教授", "20000元以上", "杭州", "浙江杭州", 0, false, null, "曾有一段3年恋情", "父亲医生，母亲退休", "一个弟弟在读研", "小康", 25, 32, 158, 168, "硕士及以上", "杭州或上海", "知性大方，有共同话题"),
                SeedMember("刘雨萱", 0, 1998, 162, 48, "本科", "设计师", "8000-12000元", "深圳", "广东广州", 0, false, null, null, "父母工薪阶层", "一个哥哥已婚", "普通", 26, 33, 168, 180, "本科及以上", "深圳或广州", "有稳定工作，性格开朗"),
                SeedMember("陈志远", 1, 1990, 180, 75, "硕士", "金融分析师", "20000元以上", "北京", "河北石家庄", 1, true, 1, "离异，孩子随母亲", "父亲企业家，母亲家庭主妇", "两个姐姐均已婚", "非常富裕", 28, 35, 160, 170, "本科及以上", "北京", "接受离异，能接受有孩子"),
                SeedMember("赵晓婷", 0, 1996, 168, 52, "本科", "护士", "5000-8000元", "成都", "四川成都", 0, false, null, null, "父母务农", "一个弟弟在打工", "普通", 28, 36, 165, 178, "大专及以上", "成都", "踏实稳重，孝顺父母"),
                SeedMember("孙伟", 1, 1993, 172, 65, "大专", "个体经营", "12000-20000元", "武汉", "湖北武汉", 0, false, null, "有过两段恋爱经历", "父母开小店", "一个妹妹在上大学", "小康", 24, 30, 158, 168, "不限", "武汉", "贤惠善良就好"),
                SeedMember("周雅琪", 0, 1999, 160, 46, "硕士", "科研人员", "8000-12000元", "南京", "江苏苏州", 0, false, null, null, "父亲工程师，母亲会计", "独生女", "小康", 27, 34, 170, 182, "硕士及以上", "南京或上海", "学历相当，有科研精神"),
                SeedMember("吴凯", 1, 1988, 176, 70, "本科", "建筑工程师", "12000-20000元", "广州", "广东佛山", 1, true, 2, "离异两年，两个孩子轮流抚养", "父母退休", "一个哥哥", "小康", 30, 38, 160, 170, "不限", "广州", "能接受有孩子，善良顾家"),
                SeedMember("林诗涵", 0, 1994, 166, 51, "本科", "教师", "5000-8000元", "厦门", "福建厦门", 0, false, null, null, "父亲渔民，母亲家庭主妇", "两个姐姐已婚", "普通", 29, 37, 168, 180, "本科及以上", "厦门或福州", "有稳定工作，热爱生活")
            )

            seeds.forEachIndexed { index, s ->
                val birthMillis = java.util.Calendar.getInstance().apply {
                    set(s.birthYear, java.util.Calendar.JUNE, 15)
                }.timeInMillis

                val reqJson = gson.toJson(mapOf(
                    "minAge" to s.reqMinAge,
                    "maxAge" to s.reqMaxAge,
                    "minHeight" to s.reqMinH,
                    "maxHeight" to s.reqMaxH,
                    "education" to s.reqEdu,
                    "city" to s.reqCity,
                    "otherRequirements" to s.reqOther
                ))

                // Generate avatar photo (fallback to empty on failure)
                val photoPathsJson = try {
                    val path = AvatarGenerator.generateAndSave(appContext, s.name, s.gender, index)
                    gson.toJson(listOf(path))
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                val timeOffset = index * 3600000L

                db.execSQL(
                    """INSERT INTO members (name, gender, birth_date, height, weight, education, occupation, income_range, city, hometown, marital_status, has_children, children_count, relationship_history, parents_info, siblings_info, family_economic_status, requirements_json, photo_paths, is_favorite, created_at, updated_at)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""",
                    arrayOf(
                        s.name, s.gender, birthMillis, s.height, s.weight,
                        s.education, s.occupation, s.income, s.city, s.hometown,
                        s.marital, if (s.hasChildren) 1 else 0, s.childrenCount,
                        s.history, s.parents, s.siblings, s.economy,
                        reqJson, photoPathsJson, if (index == 0 || index == 2) 1 else 0,
                        now - timeOffset, now - timeOffset
                    )
                )
            }
        }
    }
}
