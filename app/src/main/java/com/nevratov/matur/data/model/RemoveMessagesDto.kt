package com.nevratov.matur.data.model

import com.google.gson.annotations.SerializedName

data class RemoveMessagesDto(
    @SerializedName("im_ids") val messageId: List<Int>,
    @SerializedName("everyone") val removeEveryone: Boolean,
)
