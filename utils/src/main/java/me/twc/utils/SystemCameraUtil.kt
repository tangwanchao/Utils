package me.twc.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.UriUtils
import com.blankj.utilcode.util.UtilsTransActivity
import com.blankj.utilcode.util.UtilsTransActivity.TransActivityDelegate
import com.blankj.utilcode.util.UtilsTransActivity4MainProcess
import java.io.File
import java.io.Serializable

/**
 * @author 唐万超
 * @date 2022/02/24
 */
class SystemCameraDelegate : TransActivityDelegate() {

    companion object {
        private const val REQUEST_CODE = 100
        var sCallback: SystemCameraCallback? = null
    }

    private lateinit var mFile: File

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreated(activity: UtilsTransActivity, savedInstanceState: Bundle?) {
        super.onCreated(activity, savedInstanceState)
        mFile = FileUtil.createExternalFileFile("tcbang", ".jpeg")
        FileUtils.createOrExistsFile(mFile)
        val uri = UriUtils.file2Uri(mFile)
        val showIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        showIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        showIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        if (showIntent.resolveActivity(activity.packageManager) != null) {
            @Suppress("DEPRECATION")
            activity.startActivityForResult(showIntent, REQUEST_CODE)
        } else {
            showCenterToast("无系统相机应用,无法完成拍照")
            activity.finish()
        }
    }

    override fun onActivityResult(activity: UtilsTransActivity, requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(activity, requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && ::mFile.isInitialized) {
            sCallback?.onResult(mFile.absolutePath)
            sCallback = null
        }
        activity.finish()
    }
}

interface SystemCameraCallback : Serializable {
    fun onResult(filePath: String)
}

fun startSystemCamera(activity: Activity, callback: SystemCameraCallback) {
    SystemCameraDelegate.sCallback = callback
    val delegate = SystemCameraDelegate()
    UtilsTransActivity4MainProcess.start(activity, delegate)
}