package com.andrew.photoweatherapp.presentation

import android.content.Context
import android.media.Image
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.nio.ByteBuffer


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
    File.createTempFile(System.currentTimeMillis().toString(), ".jpeg", externalCacheDir)

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