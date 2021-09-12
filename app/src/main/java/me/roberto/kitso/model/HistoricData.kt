package me.roberto.kitso.model

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by roberto on 12/11/17.
 */
@Keep
class HistoricData(@field:Json(name = "date")val date:String,
                   @field:Json(name = "dated")val dated:String,
                   @field:Json(name = "value")val value:String,
                   @field:Json(name = "volume")val volume:String,
                   @field:Json(name = "open")val open:String,
                   @field:Json(name = "low")val low:String,
                   @field:Json(name = "high")val high:String,
                   @field:Json(name = "close")val close: String,
                   @field:Json(name = "vwap")val vwap:String)