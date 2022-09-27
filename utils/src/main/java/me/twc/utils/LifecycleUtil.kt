package me.twc.utils

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

/**
 * @author 唐万超
 * @date 2022/06/23
 */

/**
 * 创建一个倒计时 Timer,并在 Lifecycle 销毁时自动停止 Timer
 */
fun Lifecycle.createCountDownTimer(
    millisInFuture: Long,
    countDownInterval: Long,
    callback: (isFinish: Boolean, millisUntilFinished: Long) -> Unit
): CountDownTimer {
    val timer = object : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            callback.invoke(true, 0)
        }

        override fun onTick(millisUntilFinished: Long) {
            callback.invoke(false, millisUntilFinished)
        }

    }
    this.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            timer.cancel()
        }
    })
    return timer
}

@SuppressLint("SetTextI18n")
fun Lifecycle.createCountDownWithTextView(
    view: TextView,
    displayText: String = "获取验证码",
    millisInFuture: Long = 60_000L,
    countDownInterval: Long = 1_000L,
    beforeText: String = "",
    afterText: String = "s后可重发"
) {
    cancelCountDownWithTextView(view, displayText)
    view.isEnabled = false
    val timer =
        createCountDownTimer(millisInFuture, countDownInterval) { isFinish, millisUntilFinished ->
            if (millisUntilFinished == 0L || isFinish) {
                cancelCountDownWithTextView(view, displayText)
            } else {
                view.text = "${beforeText}${millisUntilFinished / 1000}$afterText"
            }
        }
    view.setTag(R.id.twc_utils_count_down_tag, timer)
    timer.start()
}

/**
 * @see createCountDownWithTextView
 */
fun Lifecycle.cancelCountDownWithTextView(
    view: TextView,
    displayText: String = "获取验证码"
) {
    val timer = view.getTag(R.id.twc_utils_count_down_tag) as? CountDownTimer
    timer?.cancel()
    view.setTag(R.id.twc_utils_count_down_tag, null)
    view.text = displayText
    view.isEnabled = true
}