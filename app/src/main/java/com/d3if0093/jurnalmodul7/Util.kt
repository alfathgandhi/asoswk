package com.d3if0093.jurnalmodul7

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LiveData
import java.lang.StringBuilder
import java.text.SimpleDateFormat

fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
        .format(systemTime).toString()
}

fun formatDiary(diary:List<Diary>,resources: Resources): Spanned {
    val sb =StringBuilder()
sb.apply{
    append(resources.getString(R.string.title))
    diary.forEach{
        append("<br>")
        append("\t${convertLongToDateString(it.tanggal)}")
        append("<br>")
        append("\t${it.diary}")
        append("<br>")

    }

}
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

