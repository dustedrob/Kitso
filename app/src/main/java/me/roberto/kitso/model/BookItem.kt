package me.roberto.kitso.model

import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@Entity
data class BookItem(

        @PrimaryKey
        @NonNull
        @field:Json(name = "book")
        val book: String,

        @field:Json(name = "minimum_price")
        val minimumPrice: String,

        @field:Json(name = "maximum_price")
        val maximumPrice: String,

        @field:Json(name = "minimum_value")
        val minimumValue: String,

        @field:Json(name = "maximum_amount")
        val maximumAmount: String,

        @field:Json(name = "maximum_value")
        val maximumValue: String,

        @field:Json(name = "minimum_amount")
        val minimumAmount: String

) {
    override fun toString(): String {
        return book.replace("_", "/").uppercase()
    }
}