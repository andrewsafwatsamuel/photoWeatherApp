package com.andrew.photoweatherapp.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.Image
import android.os.Environment.DIRECTORY_PICTURES
import android.util.Log
import android.view.View
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.nio.ByteBuffer

const val THUMBS = "thumbs"
const val PHOTOS = "photos"

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
    File("${getExternalFilesDir(DIRECTORY_PICTURES)?.absoluteFile}/$type.$time.jpg")


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

fun getBitmapFromView(view: View): Bitmap? {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

fun Bitmap.toByteArray(quality: Int): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, quality, stream)
    val byteArray = stream.toByteArray()
    stream.close()
    recycle()
    return byteArray
}
