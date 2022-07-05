package com.codesroots.satavendor.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

open class CoroutinesDispatcherProvider @Inject
constructor() {
    open val  Main: CoroutineDispatcher by lazy {Dispatchers.Main}
    open val  IO: CoroutineDispatcher by lazy {Dispatchers.IO}
}