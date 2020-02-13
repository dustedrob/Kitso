package me.roberto.kitso.model

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

/**
 * Created by roberto on 20/07/17.
 */
@Keep
@JsonClass(generateAdapter = true)
class Book(var book:String,var volume:String, var high:String, var last:String, var low:String, var vwap:String, var ask:String, var bid:String, var created_at:String)