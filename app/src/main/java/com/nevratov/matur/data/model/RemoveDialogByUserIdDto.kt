package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class RemoveDialogDto(
    @SerializedName("receiver_id") val userId: Int,
    @SerializedName("everyone") val removeEveryone: Int,
)
