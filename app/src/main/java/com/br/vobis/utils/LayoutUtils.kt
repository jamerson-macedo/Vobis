package com.br.vobis.utils

import android.content.Context

object LayoutUtils {
    fun sizeInDP(context: Context, size: Int): Int {
        return (context.resources.displayMetrics.density * size).toInt()
    }
}