package com.sata.satadelivery.helper

sealed interface UserError {
    data class NetworkError(var throwable: Throwable) : UserError
    data class UserNotFound(val id: String) : UserError
    data class InvalidId(val id: String) : UserError
    object ValidationFailed : UserError
    object ServerError : UserError
    object Unexpected : UserError
}