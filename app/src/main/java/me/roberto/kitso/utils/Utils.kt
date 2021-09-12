package me.roberto.kitso.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object Utils {


    fun stringToDate(date: String?): String? {


        val tz = TimeZone.getTimeZone("UTC")
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        df.timeZone = tz
        try {
            val parse = df.parse(date)
            val readableFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            return readableFormat.format(parse)

        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }
    }


    @ColorInt
    fun resolveColorAttr(context: Context, @AttrRes colorAttr: Int): Int {
        val resolvedAttr = resolveThemeAttr(context, colorAttr)
        val colorRes = if (resolvedAttr.resourceId != 0) resolvedAttr.resourceId else resolvedAttr.data
        return ContextCompat.getColor(context, colorRes)
    }

    fun resolveThemeAttr(context: Context, @AttrRes attrRes: Int): TypedValue {
        val theme = context.getTheme()
        val typedValue = TypedValue()
        theme.resolveAttribute(attrRes, typedValue, true)
        return typedValue
    }

}