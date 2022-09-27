package me.twc.utils

import com.blankj.utilcode.util.TimeUtils

/**
 * @author 唐万超
 * @date 2022/07/15
 */
fun String.toTimeMillisecond(): Long {
    return TimeUtils.string2Millis(this)
}

fun String.dateToTimeMillisecond():Long{
    return TimeUtils.string2Millis(this,"yyyy-MM-dd")
}