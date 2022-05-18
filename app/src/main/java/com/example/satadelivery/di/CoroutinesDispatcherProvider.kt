package com.example.satafood.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Dispatchers.Unconfined
import okhttp3.Dispatcher
import javax.inject.Inject

open class CoroutinesDispatcherProvider @Inject
constructor() {
    open val  Main: CoroutineDispatcher by lazy {Dispatchers.Main}
    open val  IO: CoroutineDispatcher by lazy {Dispatchers.IO}
}