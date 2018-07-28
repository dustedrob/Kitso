package me.roberto.kitso

import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Keep

	@Entity
data class BookItem(
    @Json(name="book")
    @PrimaryKey
	@NonNull
    var book: String,

	@Json(name="minimum_price")
	val minimumPrice: String?,

	@Json(name="maximum_price")
	val maximumPrice: String?,



	@Json(name="minimum_value")
	val minimumValue: String?,

	@Json(name="maximum_amount")
	val maximumAmount: String?,

	@Json(name="maximum_value")
	val maximumValue: String?,

	@Json(name="minimum_amount")
	val minimumAmount: String?



)
{
	override fun toString(): String {
		return book.toString().replace("_","/").toUpperCase()
	}
}