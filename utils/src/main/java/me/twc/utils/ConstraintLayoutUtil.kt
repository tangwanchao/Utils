package me.twc.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * @author 唐万超
 * @date 2021/06/21
 */
fun Flow.realAddView(parent: ConstraintLayout, view: View) {
    parent.addView(view)
    addView(view)
}

fun Flow.realRemoveView(parent: ConstraintLayout, view: View) {
    parent.removeView(view)
    removeView(view)
}

fun Flow.realRemoveViewById(parent: ConstraintLayout, @IdRes id: Int) {
    if (!referencedIds.contains(id)) {
        return
    }
    val needRemoveView = parent.findViewById<View>(id) ?: return
    realRemoveView(parent, needRemoveView)
}

fun Flow.realRemoveAllView(parent: ConstraintLayout) {
    val ids = referencedIds
    if (ids.isEmpty()) return
    for (id in ids) {
        val needRemove = parent.findViewById<View>(id)
        if (needRemove != null) {
            realRemoveView(parent, needRemove)
        }
    }
}