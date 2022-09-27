package me.twc.utils

import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ClickUtils

/**
 * @author 唐万超
 * @date 2022/06/21
 */
fun View.applySingleDebouncingAnyTime(duration: Long = 500, listener: (View) -> Unit) {
    ClickUtils.applySingleDebouncing(this, duration, listener)
}

fun View.useLayoutParams(block: (ViewGroup.LayoutParams?) -> Unit) {
    val lp = layoutParams
    block(lp)
    layoutParams = lp
}