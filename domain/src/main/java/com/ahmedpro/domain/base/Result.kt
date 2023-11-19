package com.ahmedpro.domain.base

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val errorDetails: ErrorDetails) : Result<Nothing>()
}

data class ErrorDetails(
    val code: Int = 0,
    val message: String? = null,
    val throwable: Throwable? = null,
)