package com.example.satafood.di

import kotlinx.coroutines.withContext

suspend fun <T : Any> runIO(dispatcherProvider: CoroutinesDispatcherProvider, work: suspend () -> T): ResultHandler<T> = withContext(dispatcherProvider.IO) {
    try {
        val result = work()
        withContext(dispatcherProvider.IO) {
            when (result) {
                is Throwable -> ResultHandler.Error(result)
                else -> ResultHandler.Success(result)
            }
        }
    } catch (ex: Exception) {
        withContext(dispatcherProvider.IO) {
            ResultHandler.Error(ex)
        }
    }
}

sealed class ResultHandler<out T : Any> {
    data class Success<out T : Any>(val data: T) : ResultHandler<T>()
    data class Error(val throwable: Throwable) : ResultHandler<Nothing>()
}