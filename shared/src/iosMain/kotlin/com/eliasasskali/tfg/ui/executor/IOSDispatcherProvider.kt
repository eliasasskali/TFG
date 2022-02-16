package com.eliasasskali.tfg.ui.executor

import kotlinx.coroutines.Dispatchers

class IOSDispatcherProvider : CoroutineDispatcherProvider {
    override val main = Dispatchers.Main
    override val io = Dispatchers.Main
    override val default = Dispatchers.Default
}