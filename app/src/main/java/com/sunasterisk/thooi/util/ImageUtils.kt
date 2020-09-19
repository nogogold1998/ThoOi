package com.sunasterisk.thooi.util

import android.content.Context
import android.os.Environment
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

const val DEFAULT_BITMAP_SIZE = 128

fun Context.getBitmapFromRes(@DrawableRes drawableRes: Int, size: Int = DEFAULT_BITMAP_SIZE) =
    ContextCompat.getDrawable(this, drawableRes)?.toBitmap(size, size)

@Throws(IOException::class)
fun Context?.createImageFile(): Pair<File, String> {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName: String = "JPEG_" + timeStamp + "_"
    val storageDir: File? = this?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    if (storageDir?.exists() == false) storageDir.mkdirs()
    val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
    val imageFilePath = imageFile.absolutePath
    return imageFile to imageFilePath
}
