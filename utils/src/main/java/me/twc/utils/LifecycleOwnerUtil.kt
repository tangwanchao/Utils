package me.twc.utils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope

/**
 * @author 唐万超
 * @date 2024/03/15
 */


fun LifecycleOwner.repeatOnLifecycleOnce(
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit
) = lifecycleScope.repeatOnLifecycleOnce(lifecycle, state, block)