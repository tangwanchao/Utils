package me.twc.utils

import android.graphics.Color
import android.view.Gravity
import com.blankj.utilcode.util.ToastUtils

/**
 * @author 唐万超
 * @date 2022/05/18
 */

/**
 * 在屏幕中央展示 Toast 消息
 */
fun showCenterToast(msg: String, isLong: Boolean = false) {
    if (msg.isBlank()) return
    ToastUtils.make()
        .setGravity(Gravity.CENTER, 0, 0)
        .setDurationIsLong(isLong)
        .setTextColor(Color.WHITE)
        .setBgColor(Color.BLACK)
        .show(msg)
}