package me.roberto.kitso.network

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by roberto on 17/08/17.
 */

@Keep
@JsonClass(generateAdapter = true)
data class KitsoResponse<T>(
        @Json(name = "success")
        val success: Boolean,
        @Json(name = "payload")
        val payload:T
)