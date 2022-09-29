package me.twc.utils

import android.content.res.Resources
import com.blankj.utilcode.util.AdaptScreenUtils
import com.blankj.utilcode.util.Utils


/**
 * @author 唐万超
 * @date 2022/09/29
 */

/**
 * 获取状态栏高度,如果用了 [AdaptScreenUtils] 适配,请使用 [getAdaptStatusBarHeight]
 */
fun getStatusBarHeight(): Int = getStatusBarHeight(Utils.getApp().resources)
fun getAdaptStatusBarHeight(): Int = getStatusBarHeight(Resources.getSystem())

private fun getStatusBarHeight(res: Resources): Int {
    val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
    return res.getDimensionPixelSize(resourceId)
}
