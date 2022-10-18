package me.twc.utils

import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.widget.NumberPicker
import androidx.annotation.ColorInt
import androidx.annotation.Px
import java.lang.reflect.Field

/**
 * @author 唐万超
 * @date 2021/10/13
 */
fun NumberPicker.setNumberPickerDividerColor(@ColorInt color: Int) {
    val pickerFields: Array<Field> = NumberPicker::class.java.declaredFields
    for (pf in pickerFields) {
        if (pf.name.equals("mSelectionDivider")) {
            pf.isAccessible = true
            try {
                //设置分割线的颜色值
                pf.set(this, ColorDrawable(color))
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            break
        }
    }
}

fun NumberPicker.setNumberPickerDividerHeight(@Px height: Int) {
    val pickerFields: Array<Field> = NumberPicker::class.java.declaredFields
    for (pf in pickerFields) {
        if (pf.name.equals("mSelectionDividerHeight")) {
            pf.isAccessible = true
            try {
                //设置分割线的颜色值
                pf.set(this, height)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            break
        }
    }
}