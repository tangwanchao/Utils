package me.twc.utils

import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.BarUtils
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

fun View.setPaddingAny(
    left: Int = this.paddingLeft,
    top: Int = this.paddingTop,
    right: Int = this.paddingRight,
    bottom: Int = this.paddingBottom
) = setPadding(left, top, right, bottom)

fun View.appendPadding(
    left: Int = 0,
    top: Int = 0,
    right: Int = 0,
    bottom: Int = 0
) {
    setPadding(paddingLeft + left, paddingTop + top, paddingRight + right, paddingBottom + bottom)
}