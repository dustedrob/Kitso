package me.roberto.kitso.utils

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
}