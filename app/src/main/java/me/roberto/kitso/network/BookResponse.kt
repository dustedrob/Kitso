package me.roberto.kitso.network

import androidx.annotation.Keep
import com.squareup.moshi.Json
import me.roberto.kitso.model.Book
import me.roberto.kitso.model.BookItem

@Keep
data class BookResponse(@field:Json(name = "payload")val book: Book): KitsoResponse()
