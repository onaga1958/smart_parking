package com.findparking.app.baseui

import androidx.lifecycle.ViewModel
import com.findparking.app.toolbox.CoroutineContextPool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel(), CoroutineScope {
    private val lifecycleJob = Job()

    override val coroutineContext: CoroutineContext
        get() = CoroutineContextPool.background + lifecycleJob

    override fun onCleared() {
        super.onCleared()
        lifecycleJob.cancel()
    }
}