package com.findparking.app.toolbox

import com.findparking.app.toolbox.extensions.logd
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers

object CoroutineContextPool {
    val background = Dispatchers.IO
    val ui = Dispatchers.Main

    // Default exception titleMarginHandler that can be applied to coroutines.
    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        logd("CoroutineContextPool", exception.message)
    }
}
