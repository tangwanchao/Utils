package me.twc.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.blankj.utilcode.util.Utils

/**
 * @author 唐万超
 * @date 2022/07/01
 */
@ColorInt
fun getColorInt(@ColorRes colorRes: Int, context: Context = Utils.getApp(), theme: Resources.Theme? = null): Int {
    return ResourcesCompat.getColor(context.resources, colorRes, theme)
}

fun Context.getDrawableCompat(@DrawableRes resId: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, resId, null)
}