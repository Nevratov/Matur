package com.nevratov.matur.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("gender") val gender: Int,
    @SerializedName("birthday") val birthday: String,
    @SerializedName("auth_key") val authKey: String,
    @SerializedName("city_id") val cityId: Int,
    @SerializedName("about_me") val aboutMe: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("body_type") val bodyType : Int,
    @SerializedName("education") val education: Int,
    @SerializedName("job") val job: String,
    @SerializedName("marital_status") val maritalStatus: Int,
    @SerializedName("children") val children: Int,
    @SerializedName("house") val house: Int,
    @SerializedName("nationality") val nationality: Int,
    @SerializedName("language_skills") val languageSkills: Int,
    @SerializedName("religion") val religion: Int,
    @SerializedName("religiosity_level") val religiosityLevel: Int,
    @SerializedName("expectations") val expectations: String,
    @SerializedName("drinking") val drinking: Int,
    @SerializedName("smoking") val smoking: Int
)
