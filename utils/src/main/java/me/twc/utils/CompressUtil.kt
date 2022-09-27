package me.twc.utils

import android.graphics.Bitmap
import android.util.Size
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ImageUtils
import java.io.File

/**
 * @author 唐万超
 * @date 2022/05/20
 */
/**
 * 压缩图片
 *
 * 1.使用 [maxWidth],[maxHeight] 改变图片最大大小
 * 2.使用 [rotateDegree] 改变压缩后文件方向(压缩后图片无元数据)
 * 3.使用 [maxSize] 压缩质量.
 *
 *
 * @param bytes 图片文件字节数组
 * @param maxWidth 图片最大宽度
 * @param maxHeight 图片最大高度
 * @param maxSize 图片最大大小
 * @param rotateDegree 图片的旋转角度
 * @param outputFile 输出文件
 * @param cameraViewSize 以 CenterCrop 形式裁剪图片
 *
 * @return 压缩后文件
 */
fun compressImage(
    bytes: ByteArray,
    rotateDegree: Int = 0,
    maxWidth: Int = 2000,
    maxHeight: Int = 2000,
    maxSize: Long = 795648L,
    outputFile: File? = null,
    cameraViewSize: Size? = null
): File? {
    try {
        var bitmap = ImageUtils.getBitmap(bytes, 0, maxWidth, maxHeight)
        if (rotateDegree != 0) {
            bitmap = ImageUtils.rotate(bitmap, rotateDegree, bitmap.width / 2f, bitmap.height / 2f, false)
        }
        val bw = bitmap.width
        val bh = bitmap.height
        if (cameraViewSize != null && cameraViewSize.width / cameraViewSize.height != bw / bh) {
            val cwh = cameraViewSize.width / cameraViewSize.height
            val bwh = bw / bh
            bitmap = if (cwh > bwh) {
                val brh = bw / cwh
                Bitmap.createBitmap(bitmap, 0, (bh - brh) / 2, bw, brh)
            } else {
                val brw = cwh * bh
                Bitmap.createBitmap(bitmap, (bw - brw) / 2, 0, brw, bh)
            }
        }
        val byteArray = ImageUtils.compressByQuality(bitmap, maxSize)
        outputFile?.let(FileUtils::createOrExistsFile)
        val file = outputFile ?: FileUtil.createCacheFile("compress", ".jpeg")
        file.writeBytes(byteArray)
        return file
    } catch (th: Throwable) {
        th.printStackTrace()
    }
    return null
}