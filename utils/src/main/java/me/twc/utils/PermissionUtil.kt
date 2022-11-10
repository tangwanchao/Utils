@file:Suppress("unused")

package me.twc.utils

import android.content.Context
import android.content.pm.PackageManager
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.blankj.utilcode.util.IntentUtils

/**
 * @author 唐万超
 * @date 2022/11/08
 */

typealias ShowAlterFunction = (
    context: Context,
    lifecycleOwner: LifecycleOwner,
    message: String,
    theme: Int,
    positiveCallback: () -> Unit
) -> Unit

object PermissionUtil {
    /**
     * 创建对话框方法,设置此方法后将使用此方法创建
     * "索要权限说明对话框" 和 "用户永久拒绝提示对话框"
     */
    var mShowAlterFunction: ShowAlterFunction? = null

    /**
     * "索要权限说明对话框"默认消息
     */
    var mDefaultRationaleMessage = ""

    /**
     * "用户永久拒绝提示对话框"默认消息
     */
    var mDefaultDeniedForeverMessage = "应用无权限,请前往设置页面开启相关权限"

    /**
     * 无法前往设置页面 toast 默认消息
     */
    var mDefaultCantToSettingMessage = "应用无权限,请前往设置页面开启相关权限"

    /**
     * 提示对话框默认主题
     */
    var mDefaultAlterDialogTheme = 0
}

class RequestPermissionsLauncher(
    private val mPermissions: Array<String>,
    private val mRationaleMessage: String,
    private val mRationaleDialogTheme: Int,
    private val mLauncher: ActivityResultLauncher<Array<String>>
) {
    fun launch(activity: ComponentActivity) = launch(activity, activity, activity)
    fun launch(fragment: Fragment) = launch(fragment.requireActivity(), fragment, fragment.requireContext())

    private fun launch(
        activity: ComponentActivity,
        lifecycleOwner: LifecycleOwner,
        context: Context
    ) {
        var shouldShowRationale = false
        for (permission in mPermissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                shouldShowRationale = true
                break
            }
        }
        if (shouldShowRationale && mRationaleMessage.isNotBlank()) {
            showAlterDialog(context, lifecycleOwner, mRationaleMessage, mRationaleDialogTheme) {
                mLauncher.launch(mPermissions)
            }
            return
        }
        mLauncher.launch(mPermissions)
    }

    fun onSettingComplete(activity: ComponentActivity) {
        if (mPermissions.all { ActivityCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED }) {
            mLauncher.launch(mPermissions)
        }
    }
}

fun ComponentActivity.registerForPermissions(
    permissions: Array<String>,
    rationaleMessage: String = PermissionUtil.mDefaultRationaleMessage,
    deniedForeverMessage: String = PermissionUtil.mDefaultDeniedForeverMessage,
    cantToSettingMessage: String = PermissionUtil.mDefaultCantToSettingMessage,
    theme: Int = PermissionUtil.mDefaultAlterDialogTheme,
    onAllGrant: () -> Unit
): RequestPermissionsLauncher = internalRegisterForPermissions(
    permissions,
    rationaleMessage,
    deniedForeverMessage,
    cantToSettingMessage,
    theme,
    onAllGrant
)

fun Fragment.registerForPermissions(
    permissions: Array<String>,
    rationaleMessage: String = PermissionUtil.mDefaultRationaleMessage,
    deniedForeverMessage: String = PermissionUtil.mDefaultDeniedForeverMessage,
    cantToSettingMessage: String = PermissionUtil.mDefaultCantToSettingMessage,
    theme: Int = PermissionUtil.mDefaultAlterDialogTheme,
    onAllGrant: () -> Unit
): RequestPermissionsLauncher = internalRegisterForPermissions(
    permissions,
    rationaleMessage,
    deniedForeverMessage,
    cantToSettingMessage,
    theme,
    onAllGrant
)

//<editor-fold desc="私有拓展">
/**
 * @param permissions 需要请求的权限数组
 * @param rationaleMessage 索取权限理由消息,在需要的时候将自动弹出索取权限理由对话框.为空不展示
 * @param deniedForeverMessage 用户永久拒绝权限消息,用户永久拒绝权限后将弹出对话框,用户确定后将跳转到设置界面.为空不展示
 * @param cantToSettingMessage 用户永久拒绝权限后,无法跳转到设置界面 Toast 消息.为空不展示
 * @param theme 对话框主题
 * @param onAllGrant 成功获取所有权限后回调
 */
private fun Any.internalRegisterForPermissions(
    permissions: Array<String>,
    rationaleMessage: String,
    deniedForeverMessage: String,
    cantToSettingMessage: String,
    theme: Int,
    onAllGrant: () -> Unit
): RequestPermissionsLauncher {
    var deniedForeverCallback: (() -> Unit)? = null
    // 请求权限 launcher
    val contract = ActivityResultContracts.RequestMultiplePermissions()
    val contractCallback = ActivityResultCallback<Map<String, Boolean>> { output ->
        if (output.isEmpty()) return@ActivityResultCallback
        if (output.values.all { it }) {
            onAllGrant()
            return@ActivityResultCallback
        }
        val act = getActivity()
        if (act != null && deniedForeverMessage.isNotBlank()) {
            val find = output.keys.find { permission -> !ActivityCompat.shouldShowRequestPermissionRationale(act, permission) }
            if (find != null) {
                deniedForeverCallback?.invoke()
                return@ActivityResultCallback
            }
        }
    }
    val launcher = registerForActivityResult(contract, contractCallback)
    val requestPermissionLauncher = RequestPermissionsLauncher(permissions, rationaleMessage, theme, launcher)
    // 设置 launcher
    val settingContract = ActivityResultContracts.StartActivityForResult()
    val settingContractCallback = ActivityResultCallback<ActivityResult> {
        val act = getActivity()
        if (act != null) {
            requestPermissionLauncher.onSettingComplete(act)
        }
    }
    val settingLauncher = registerForActivityResult(settingContract, settingContractCallback)
    if (deniedForeverMessage.isNotBlank()) {
        deniedForeverCallback = deniedForever@{
            val context = getContext()
            val lifecycleOwner = getLifecycleOwner()
            if (context == null || lifecycleOwner == null) {
                return@deniedForever
            }
            val intent = IntentUtils.getLaunchAppDetailsSettingsIntent(context.packageName)
            if (IntentUtils.isIntentAvailable(intent)) {
                showAlterDialog(context, lifecycleOwner, deniedForeverMessage, theme) {
                    settingLauncher.launch(intent)
                }
            } else {
                showCenterToast(cantToSettingMessage, true)
            }
        }
    }
    return requestPermissionLauncher
}

private fun Any.getActivity(): ComponentActivity? {
    if (this is AppCompatActivity) return this
    if (this is Fragment) return activity
    return null
}

private fun Any.getContext(): Context? {
    if (this is AppCompatActivity) return this
    if (this is Fragment) return context
    return null
}

private fun Any.getLifecycleOwner(): LifecycleOwner? {
    if (this is AppCompatActivity) return this
    if (this is Fragment) return this
    return null
}

private fun <I, O> Any.registerForActivityResult(
    contract: ActivityResultContract<I, O>,
    callback: ActivityResultCallback<O>,
): ActivityResultLauncher<I> {
    return when (this) {
        is ComponentActivity -> this.registerForActivityResult(contract, callback)
        is Fragment -> this.registerForActivityResult(contract, callback)
        else -> throw IllegalArgumentException("only support activity or fragment")
    }
}

private fun showAlterDialog(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    message: String,
    theme: Int = androidx.appcompat.R.style.Theme_AppCompat_Light_NoActionBar,
    positiveCallback: () -> Unit
) {
    val func = PermissionUtil.mShowAlterFunction
    if (func != null) {
        func(context, lifecycleOwner, message, theme, positiveCallback)
        return
    }
    val dialog = AlertDialog.Builder(context, theme)
        .setMessage(message)
        .setNegativeButton("取消") { dialog, _ ->
            dialog.cancel()
        }
        .setPositiveButton("确定") { dialog, _ ->
            dialog.cancel()
            positiveCallback()
        }
        .create()
        .apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            show()
        }
    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            dialog.cancel()
        }
    })
}
//</editor-fold>