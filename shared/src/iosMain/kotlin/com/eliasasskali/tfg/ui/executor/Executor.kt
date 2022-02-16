package com.eliasasskali.tfg.ui.executor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import platform.Foundation.NSRunLoop
import platform.Foundation.performBlock
import kotlin.coroutines.CoroutineContext

actual class Executor(
    coroutineDispatcherProvider: CoroutineDispatcherProvider = IOSDispatcherProvider()
) {
    actual val main: CoroutineDispatcher = coroutineDispatcherProvider.main
    actual val bg: CoroutineDispatcher = coroutineDispatcherProvider.default
}

object Main : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        NSRunLoop.mainRunLoop().performBlock {
            block.run()
        }
    }
}