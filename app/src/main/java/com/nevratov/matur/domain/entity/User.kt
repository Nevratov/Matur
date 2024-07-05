package com.nevratov.matur.domain.entity

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class User(
    val id: Int,
    val name: String,
    val logoUrl: String?,
    val gender: String,
    val birthday: String,
    val cityId: Int,
    val aboutMe: String?,
    val height: Int,
    val weight: Int,
    val bodyType : String,
    val education: String,
    val job: String?,
    val maritalStatus: String,
    val children: String,
    val house: String,
    val nationality: String,
    val languageSkills: String,
    val religion: String,
    val religiosityLevel: String,
    val expectations: String?,
    val drinking: String,
    val smoking: String
): Parcelable
