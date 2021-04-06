package com.andrew.photoweatherapp.presentation

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.Image
import android.media.ThumbnailUtils
import android.net.ConnectivityManager
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.ByteBuffer

const val THUMBS = "thumbs"
const val PHOTOS = "photos"

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun File.save(bytes: ByteArray) {
    var output: OutputStream? = null
    try {
        output = FileOutputStream(this)
        output.write(bytes)
    } catch (e: Exception) {
        Log.e("CameraFragment", e.message, e)
    } finally {
        output?.close()
    }
}

fun Context.createTempFile(): File =
    File.createTempFile("temporary", ".jpeg", externalCacheDir)

fun Context.createFile(type: String, time: String): File =
    File("${getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absoluteFile}/$type.$time.jpg")

fun Image.toByteArray(): ByteArray {
    var byteArray = byteArrayOf()
    try {
        val buffer: ByteBuffer = planes[0].buffer
        byteArray = ByteArray(buffer.capacity())
        buffer.get(byteArray)
    } catch (e: Exception) {
        Log.e("CameraFragment", e.message, e)
    } finally {
        close()
    }
    return byteArray
}

suspend fun Context.saveCapturedPhoto(view: View): String {
    val time = System.currentTimeMillis().toString()
    val photoBitmap = getBitmapFromView(view)
    val thumbnail = ThumbnailUtils.extractThumbnail(photoBitmap, 430, 700)
    val normalSize = createFile(PHOTOS, time)
    withContext(Dispatchers.IO) {
        createFile(THUMBS, time).save(thumbnail?.toByteArray() ?: byteArrayOf())
        normalSize.save(photoBitmap?.toByteArray() ?: byteArrayOf())
    }
    photoBitmap?.recycle()
    return normalSize.path
}

fun getBitmapFromView(view: View): Bitmap? {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteArray = stream.toByteArray()
    stream.close()
    return byteArray
}

@Suppress("DEPRECATION")
fun Context.checkConnectivity() =
    (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo?.isConnected
        ?: false

fun Activity.hideKeyboard() =
    (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .takeIf { currentFocus != null }
        ?.run { hideSoftInputFromWindow(currentFocus!!.windowToken, 0) }
