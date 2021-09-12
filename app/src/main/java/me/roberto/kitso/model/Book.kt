package me.roberto.kitso.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import me.roberto.kitso.network.KitsoResponse

/**
 * Created by roberto on 20/07/17.
 */
@Keep
data class Book(
    @field:Json(name = "book")val book:String,
    @field:Json(name = "volume")val volume:String,
    @field:Json(name = "high")val high:String,
    @field:Json(name = "last")val last:String,
    @field:Json(name = "low")val low:String,
    @field:Json(name = "vwap")val vwap:String,
    @field:Json(name = "ask")val ask:String,
    @field:Json(name = "bid")val bid:String,
    @field:Json(name = "created_at")val created_at:String): KitsoResponse()