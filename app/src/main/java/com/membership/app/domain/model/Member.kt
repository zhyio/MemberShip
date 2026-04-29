package com.membership.app.domain.model

import java.util.Date

data class Member(
    val id: Long = 0,

    // 基础信息
    val name: String,
    val gender: Int, // 0=女, 1=男
    val birthDate: Date? = null,
    val height: Int? = null, // cm
    val weight: Int? = null, // kg
    val education: String? = null,
    val occupation: String? = null,
    val incomeRange: String? = null,
    val city: String? = null,
    val hometown: String? = null,

    // 婚姻情感
    val maritalStatus: Int = 0, // 0=未婚, 1=离异, 2=丧偶
    val hasChildren: Boolean = false,
    val childrenCount: Int? = null,
    val relationshipHistory: String? = null,

    // 家庭信息
    val parentsInfo: String? = null,
    val siblingsInfo: String? = null,
    val familyEconomicStatus: String? = null,

    // 择偶要求
    val requirements: PartnerRequirements? = null,

    // 照片
    val photos: List<String> = emptyList(),

    // 收藏
    val isFavorite: Boolean = false,

    // 元数据
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) {
    val age: Int?
        get() = birthDate?.let {
            val now = Date()
            val diff = now.time - it.time
            (diff / (1000 * 60 * 60 * 24 * 365.25)).toInt()
        }

    val genderText: String
        get() = if (gender == 1) "男" else "女"

    val maritalStatusText: String
        get() = when (maritalStatus) {
            1 -> "离异"
            2 -> "丧偶"
            else -> "未婚"
        }

    val displayHeight: String
        get() = height?.let { "${it}cm" } ?: ""

    val displayWeight: String
        get() = weight?.let { "${it}kg" } ?: ""

    val bodyInfo: String
        get() = listOfNotNull(
            height?.let { "${it}cm" },
            weight?.let { "${it}kg" }
        ).joinToString(" / ")
}

data class PartnerRequirements(
    val minAge: Int? = null,
    val maxAge: Int? = null,
    val minHeight: Int? = null,
    val maxHeight: Int? = null,
    val education: String? = null,
    val maritalStatus: Int? = null, // null=不限, 0=未婚, 1=离异, 2=丧偶
    val hasChildren: Boolean? = null, // null=不限, true=接受有子女
    val city: String? = null,
    val otherRequirements: String? = null
) {
    val ageRange: String?
        get() = when {
            minAge != null && maxAge != null -> "$minAge-${maxAge}岁"
            minAge != null -> "${minAge}岁以上"
            maxAge != null -> "${maxAge}岁以下"
            else -> null
        }

    val heightRange: String?
        get() = when {
            minHeight != null && maxHeight != null -> "$minHeight-${maxHeight}cm"
            minHeight != null -> "${minHeight}cm以上"
            maxHeight != null -> "${maxHeight}cm以下"
            else -> null
        }
}
