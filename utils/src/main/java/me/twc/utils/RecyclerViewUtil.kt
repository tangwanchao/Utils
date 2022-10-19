package me.twc.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * @author 唐万超
 * @date 2022/10/17
 */
/**
 * 点击的条目滑动到顶部
 */
fun RecyclerView.smoothScrollClickedItemToTop(position: Int) {
    val layout = layoutManager as? LinearLayoutManager ?: return
    val adapter = adapter ?: return
    val firstVisibleItemPosition = layout.findFirstVisibleItemPosition()
    val lastVisibleItemPosition = layout.findLastVisibleItemPosition()

    if (adapter.itemCount - 1 == lastVisibleItemPosition) {
        val bottom = getChildAt(lastVisibleItemPosition - firstVisibleItemPosition).bottom
        if (bottom == height) {
            return
        }
    }
    if (position in firstVisibleItemPosition..lastVisibleItemPosition) {
        val top = getChildAt(position - firstVisibleItemPosition).top
        smoothScrollBy(0, top)
    }
}

fun RecyclerView.smoothScrollToItem(position: Int, snap: Int = LinearSmoothScroller.SNAP_TO_START) = safeDo {
    val lm = layoutManager ?: return@safeDo
    if (lm !is LinearLayoutManager) return@safeDo
    val scroller = object : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference(): Int = snap
    }
    scroller.targetPosition = position
    lm.startSmoothScroll(scroller)
}