package me.roberto.kitso.network

import androidx.annotation.Keep
import com.squareup.moshi.Json
import me.roberto.kitso.model.BookItem

@Keep
data class AvailableBooksResponse(@field:Json(name = "payload")val books:List<BookItem>): KitsoResponse()
