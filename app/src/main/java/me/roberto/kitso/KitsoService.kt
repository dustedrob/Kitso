package me.roberto.kitso

import me.roberto.kitso.model.Book
import me.roberto.kitso.model.BookItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by roberto on 20/07/17.
 */
interface KitsoService {

    @GET("ticker")
    fun getTicker(@Query("book") book: String): Call<KitsoResponse<Book>>

    @GET("order_book")
    fun getOrderBook(@Query("book") book: String): Call<KitsoResponse<OrderBook>>

    @GET("available_books")
    fun getAvailableBooks(): Call<KitsoResponse<List<BookItem>>>

}