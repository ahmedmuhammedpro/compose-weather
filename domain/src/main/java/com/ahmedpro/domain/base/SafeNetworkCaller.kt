package com.ahmedpro.domain.base

suspend fun <T> request(apiCall: suspend () -> Result<T>): Result<T> {
    return try {
        apiCall.invoke()
    } catch (th: Throwable) {
        Result.Error(ErrorDetails(throwable = th))
    }
}