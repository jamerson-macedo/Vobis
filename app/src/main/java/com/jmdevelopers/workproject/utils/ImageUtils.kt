package com.jmdevelopers.workproject.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class ImageUtils {
    companion object {
        fun compressImage(bitmap: Bitmap): ByteArrayInputStream {
            val baos = ByteArrayOutputStream()

            bitmap.compress(Bitmap.CompressFormat.WEBP, 50, baos)
            return ByteArrayInputStream(baos.toByteArray())
        }

        fun selectByGallery(activity: Activity, codeRequest: Int) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            activity.startActivityForResult(intent, codeRequest)
        }
    }
}