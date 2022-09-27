package me.twc.utils

import android.app.Activity
import android.graphics.Color
import android.view.View
import androidx.annotation.ColorInt
import com.blankj.utilcode.util.BarUtils

/**
 * @author 唐万超
 * @date 2022/06/23
 */
fun Activity.whiteStateBar() = customStateBar(Color.WHITE, true)

fun Activity.blankStateBar() = customStateBar(Color.BLACK, false)

/**
 * 自定义状态栏颜色
 *
 * @param color 状态栏颜色
 * @param isLightMode [true : 状态栏启用明亮模式]
 *                    [false: 其他情况]
 */
fun Activity.customStateBar(@ColorInt color: Int, isLightMode: Boolean) {
    window.statusBarColor = color
    BarUtils.setStatusBarLightMode(window, isLightMode)
}

/**
 * 布局填充整个屏幕
 */
fun Activity.layoutFullScreen() {
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    window.statusBarColor = Color.TRANSPARENT
}