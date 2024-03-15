package me.twc.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * @author 唐万超
 * @date 2024/03/15
 */

/**
 * 仅运行一次的 repeatOnLifecycle
 */
fun LifecycleCoroutineScope.repeatOnLifecycleOnce(
    lifecycle: Lifecycle,
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit
) = launch {
    lifecycle.repeatOnLifecycle(state) {
        block()
        this@launch.cancel()
    }
}