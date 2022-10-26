package me.twc.utils

import android.text.InputFilter
import android.text.Spanned
import android.widget.EditText
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

fun EditText.setTextCursorEnd(text: CharSequence) {
    setText(text)
    safeDo { setSelection(text.length) }
}

//<editor-fold desc="InputFilter">
fun TextView.addIntPriceFilter() {
    filters = filters.toMutableList().let {
        it.add(IntPriceFilter())
        it.toTypedArray()
    }
}

fun TextView.addRegexReplaceInputFilter(regex: Regex) {
    filters = filters.toMutableList().let {
        it.add(RegexReplaceInputFilter(regex))
        it.toTypedArray()
    }
}

/**
 * Int 类型价格输入过滤
 *
 * 此过滤器不负责过滤输入,请在添加此过滤前设置 inputType="number"
 */
class IntPriceFilter : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        // 第一位不是 1-9 的输入全部替换为 0
        if (dstart == 0) {
            var ret = source?.subSequence(start, end)
            val regex = Regex("[^1-9]")
            while (!ret.isNullOrEmpty() && regex.find(ret.subSequence(0, 1)) != null) {
                ret = ret.replaceFirst(regex, "")
            }
            return ret
        }
        return null
    }
}

/**
 * 正则过滤器
 *
 * 符合正则表达式的字符将会被替换为 ""
 */
class RegexReplaceInputFilter(
    private val mRegex: Regex
) : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        return source?.subSequence(start, end)
            ?.replace(mRegex, "")
    }
}
//</editor-fold>