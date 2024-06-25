package com.nevratov.matur.data.network.webSocket

sealed class TypeResponseWS(val type: String) {

    data object Message: TypeResponseWS(MESSAGE_TYPE)
    data object Status: TypeResponseWS(STATUS_TYPE)
    data object ReadAll: TypeResponseWS(READ_ALL_TYPE)

    companion object {
        private const val MESSAGE_TYPE = "message"
        private const val STATUS_TYPE = "status"
        private const val READ_ALL_TYPE = "read_all"
    }
}