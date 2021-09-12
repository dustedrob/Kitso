package me.roberto.kitso.model

import androidx.annotation.Keep
import com.squareup.moshi.Json

/**
 * Created by roberto on 12/11/17.
 */
@Keep
data class Order (
    @field:Json(name = "book")var book:String,
    @field:Json(name = "price")var price:String,
    @field:Json(name = "amount")var amount:String)