package me.roberto.kitso

import android.support.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class BookItem(
    @Json(name="book")
    val book: String?,

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