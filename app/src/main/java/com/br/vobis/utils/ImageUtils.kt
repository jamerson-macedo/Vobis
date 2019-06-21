package com.br.vobis.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.LinearLayout
import com.br.vobis.utils.LayoutUtils.sizeInDP
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object ImageUtils {
    fun compressImage(bitmap: Bitmap): ByteArrayInputStream {
        val baos = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.WEBP, 50, baos)
        return ByteArrayInputStream(baos.toByteArray())
    }

    fun selectImageByGallery(context: Activity, codeRequest: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)

        intent.type = "image/*"

        context.startActivityForResult(intent, codeRequest)
    }

    fun generateImage(context: Context, bitmap: Bitmap, width: Int, height: Int, ml: Int = 0, mr: Int = 0, mt: Int = 0, mb: Int = 0): ImageView {
        val sizeInDP = { size: Int -> sizeInDP(context, size) }
        val newImage = ImageView(context)
        val layoutParams = LinearLayout.LayoutParams(sizeInDP(width), sizeInDP(height))

        layoutParams.leftMargin = sizeInDP(ml)
        layoutParams.topMargin = sizeInDP(mt)
        layoutParams.rightMargin = sizeInDP(mr)
        layoutParams.bottomMargin = sizeInDP(mb)

        newImage.setImageBitmap(bitmap)
        newImage.scaleType = ImageView.ScaleType.CENTER_CROP
        newImage.layoutParams = layoutParams

        return newImage
    }
}