package com.andrew.photoweatherapp.presentation

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

const val TWITTER_PACKAGE = "com.twitter.android"
const val FACEBOOK_PACKAGE = "com.facebook.katana"

fun shareImage(context: Context, imagePath: String, packageId: String) =
    if (context.packageManager.getLaunchIntentForPackage(packageId) == null) {
        Toast.makeText(context, "Please first install the app", Toast.LENGTH_SHORT).show()
        context.startPlayStore(packageId)
    } else {
        imagePath.takeIf { it.isNotBlank() }
            ?.let { context.composeImageUri(imagePath) }
            ?.let { context.startIntent(packageId, it) }
    }

private fun Context.startIntent(packageId: String, uri: Uri) = try {
    startShareIntent(packageId, uri)
} catch (ex: Exception) {
    Toast.makeText(this, "Please first install the app", Toast.LENGTH_SHORT).show()
    startPlayStore(packageId)
}

private fun Context.startShareIntent(
    packageId: String,
    uri: Uri
) = Intent(Intent.ACTION_SEND).apply {
    setPackage(packageId)
    type = "image/*"
    putExtra(Intent.EXTRA_STREAM, uri)
}.let { startActivity(it) }

private fun Context.startPlayStore(packageId: String) = Intent(
    ACTION_VIEW,
    Uri.parse("https://play.google.com/store/apps/details?id=$packageId")
).let { startActivity(it) }

private fun Context.composeImageUri(imagePath: String): Uri =
    "${applicationContext.packageName}.provider"
        .let { FileProvider.getUriForFile(this, it, File(imagePath)) }
