package me.twc.utils


/**
 * @author 唐万超
 * @date 2020/11/18
 */
inline fun <R> safeDo(block: () -> R?): R? {
    try {
        return block()
    } catch (th: Throwable) {
        th.printStackTrace()
    }
    return null
}