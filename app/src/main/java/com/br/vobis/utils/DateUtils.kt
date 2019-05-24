package com.br.vobis.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*


class DateUtils {
    companion object {
        fun formatDate(date: Timestamp): String? {
            val locale = Locale("pt", "BR")
            val tz = TimeZone.getTimeZone("UTC")
            val df = SimpleDateFormat("dd/MM/yyyy", locale)
            df.timeZone = tz
            return df.format(Date(date.seconds * 1000L))
        }
    }
}