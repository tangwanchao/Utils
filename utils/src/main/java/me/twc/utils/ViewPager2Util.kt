package me.twc.utils

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * @author 唐万超
 * @date 2022/07/22
 */
@SuppressLint("ClickableViewAccessibility")
fun ViewPager2.fixBugWithSmartRefreshLayout() {
    (getChildAt(0) as RecyclerView).setOnTouchListener(object : View.OnTouchListener {
        private var mStartX = 0f
        private var mStartY = 0f

        // 是否是竖方向滑动
        private var mIsVerticalDrag = false
        private val mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        override fun onTouch(v: View, ev: MotionEvent): Boolean {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    mStartX = ev.x
                    mStartY = ev.y
                    mIsVerticalDrag = false
                }
                MotionEvent.ACTION_MOVE -> {
                    // 关键代码(可能没有 ACTION_DOWN)
                    if (mStartX == 0f || mStartY == 0f) {
                        mStartX = ev.x
                        mStartY = ev.y
                        mIsVerticalDrag = false
                        // 关键代码(返回 true 后才能继续拿到事件)
                        return true
                    }
                    if (mIsVerticalDrag) return true
                    val endX = ev.x
                    val endY = ev.y
                    val disX = abs(endX - mStartX)
                    val disY = abs(endY - mStartY)
                    if (disY > mTouchSlop && disY > disX) {
                        mIsVerticalDrag = true
                        return true
                    }
                }
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    mStartX = 0f
                    mStartY = 0f
                    val preIsVerticalDrag = mIsVerticalDrag
                    mIsVerticalDrag = false
                    // 关键代码,竖直方向活动可能导致 ViewPager2 有细微(肉眼不可见)的滑动,导致 ViewPager2 滑动状态变更
                    // 此时 SmartRefreshLayout 拿不到事件导致不能滑动.我们这里手动滑动到当前页,使 ViewPager2 内部状态变更为闲置
                    if (preIsVerticalDrag) {
                        this@fixBugWithSmartRefreshLayout.setCurrentItem(this@fixBugWithSmartRefreshLayout.currentItem, false)
                    }
                    return preIsVerticalDrag
                }
            }
            return false
        }
    })
}