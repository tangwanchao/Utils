package me.twc.utils

import com.blankj.utilcode.util.AdaptScreenUtils
import java.text.NumberFormat

/**
 * @author 唐万超
 * @date 2022/06/23
 */
val Number.pt
    get() = try {
        AdaptScreenUtils.pt2Px(this.toFloat())
    } catch (th: Throwable) {
        th.printStackTrace()
        this.toFloat().toInt()
    }

object NumberUtil {
    private val mNumberFormat by lazy { NumberFormat.getNumberInstance() }

    /**
     * 小数位数格式化
     *
     * @param number 要格式化的数字
     * @param min 最小位数
     * @param max 最大位数
     */
    fun integerDigitsFormat(number: Long, min: Int, max: Int, isGroupingUsed: Boolean = false): String {
        mNumberFormat.minimumIntegerDigits = min
        mNumberFormat.maximumIntegerDigits = max
        mNumberFormat.isGroupingUsed = isGroupingUsed
        return mNumberFormat.format(number)
    }

    /**
     * 小数位数格式化
     *
     * @param number 要格式化的数字
     * @param min 最小位数
     * @param max 最大位数
     */
    fun integerDigitsFormat(number: Double, min: Int, max: Int, isGroupingUsed: Boolean = false): String {
        mNumberFormat.minimumIntegerDigits = min
        mNumberFormat.maximumIntegerDigits = max
        mNumberFormat.isGroupingUsed = isGroupingUsed
        return mNumberFormat.format(number)
    }
}