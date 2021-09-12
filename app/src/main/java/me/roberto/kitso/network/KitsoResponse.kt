package me.roberto.kitso.network

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by roberto on 17/08/17.
 */

@Keep
abstract class KitsoResponse
        @Json(name = "success")
        var success: Boolean = false
