package me.twc.utils

import android.widget.TextView
import com.blankj.utilcode.util.ClipboardUtils

/**
 * @author 唐万超
 * @date 2022/06/23
 */
/**
 * 检查字段是否为空
 *
 * @return EditText.text or "" or null
 */
fun TextView.checkField(message: CharSequence): String? {
    val text = this.text?.toString()?.trim() ?: ""
    if (text.isBlank()) {
        showCenterToast(message.toString())
        return null
    }
    return text
}

fun TextView.clickCopy() {
    this.applySingleDebouncingAnyTime {
        copyText()
    }
}

fun TextView.copyText() {
    ClipboardUtils.copyText(this.text ?: "")
    showCenterToast("复制成功")
}