package me.twc.utils

import android.os.Environment
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.Utils
import java.io.File

/**
 * @author 唐万超
 * @date 2020/09/10
 */
object FileUtil {
    /**
     * 创建临时文件
     */
    fun createCacheFile(prefix: String = "", suffix: String = ""): File {
        val dir = Utils.getApp().cacheDir
        FileUtils.createOrExistsDir(dir)
        return File(dir, "$prefix${System.currentTimeMillis()}$suffix")
    }

    /**
     * 创建外部共享文件
     */
    fun createExternalFileFile(type: String? = Environment.DIRECTORY_PICTURES, prefix: String = "", suffix: String = ""): File = safeDo {
        val dir = Utils.getApp().getExternalFilesDir(type)
        FileUtils.createOrExistsDir(dir)
        return@safeDo File(dir, "$prefix${System.currentTimeMillis()}$suffix")
    } ?: createCacheFile(prefix, suffix)
}