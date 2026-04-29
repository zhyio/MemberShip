package com.membership.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.membership.app.domain.model.Member
import com.membership.app.domain.model.PartnerRequirements
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "gender")
    val gender: Int, // 0=女, 1=男

    @ColumnInfo(name = "birth_date")
    val birthDate: Long? = null,

    @ColumnInfo(name = "height")
    val height: Int? = null,

    @ColumnInfo(name = "weight")
    val weight: Int? = null,

    @ColumnInfo(name = "education")
    val education: String? = null,

    @ColumnInfo(name = "occupation")
    val occupation: String? = null,

    @ColumnInfo(name = "income_range")
    val incomeRange: String? = null,

    @ColumnInfo(name = "city")
    val city: String? = null,

    @ColumnInfo(name = "hometown")
    val hometown: String? = null,

    @ColumnInfo(name = "marital_status")
    val maritalStatus: Int = 0,

    @ColumnInfo(name = "has_children")
    val hasChildren: Boolean = false,

    @ColumnInfo(name = "children_count")
    val childrenCount: Int? = null,

    @ColumnInfo(name = "relationship_history")
    val relationshipHistory: String? = null,

    @ColumnInfo(name = "parents_info")
    val parentsInfo: String? = null,

    @ColumnInfo(name = "siblings_info")
    val siblingsInfo: String? = null,

    @ColumnInfo(name = "family_economic_status")
    val familyEconomicStatus: String? = null,

    @ColumnInfo(name = "requirements_json")
    val requirementsJson: String? = null,

    @ColumnInfo(name = "photo_paths")
    val photoPaths: String? = null,

    @ColumnInfo(name = "is_favorite", defaultValue = "0")
    val isFavorite: Boolean = false,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
) {
    companion object {
        private val gson = Gson()

        fun fromMember(member: Member): MemberEntity {
            return MemberEntity(
                id = member.id,
                name = member.name,
                gender = member.gender,
                birthDate = member.birthDate?.time,
                height = member.height,
                weight = member.weight,
                education = member.education,
                occupation = member.occupation,
                incomeRange = member.incomeRange,
                city = member.city,
                hometown = member.hometown,
                maritalStatus = member.maritalStatus,
                hasChildren = member.hasChildren,
                childrenCount = member.childrenCount,
                relationshipHistory = member.relationshipHistory,
                parentsInfo = member.parentsInfo,
                siblingsInfo = member.siblingsInfo,
                familyEconomicStatus = member.familyEconomicStatus,
                requirementsJson = member.requirements?.let { gson.toJson(it) },
                photoPaths = member.photos.takeIf { it.isNotEmpty() }?.let { gson.toJson(it) },
                isFavorite = member.isFavorite,
                createdAt = member.createdAt.time,
                updatedAt = member.updatedAt.time
            )
        }
    }

    fun toMember(): Member {
        return Member(
            id = id,
            name = name,
            gender = gender,
            birthDate = birthDate?.let { Date(it) },
            height = height,
            weight = weight,
            education = education,
            occupation = occupation,
            incomeRange = incomeRange,
            city = city,
            hometown = hometown,
            maritalStatus = maritalStatus,
            hasChildren = hasChildren,
            childrenCount = childrenCount,
            relationshipHistory = relationshipHistory,
            parentsInfo = parentsInfo,
            siblingsInfo = siblingsInfo,
            familyEconomicStatus = familyEconomicStatus,
            requirements = requirementsJson?.let {
                gson.fromJson(it, PartnerRequirements::class.java)
            },
            photos = photoPaths?.let {
                val type = object : TypeToken<List<String>>() {}.type
                gson.fromJson<List<String>>(it, type)
            } ?: emptyList(),
            isFavorite = isFavorite,
            createdAt = Date(createdAt),
            updatedAt = Date(updatedAt)
        )
    }
}
