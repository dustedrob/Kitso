package me.roberto.kitso.network

import me.roberto.kitso.model.OrderBook
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by roberto on 20/07/17.
 */
interface KitsoService {

    @GET("ticker")
    fun getTicker(@Query("book") book: String): Call<BookResponse>

    @GET("order_book")
    fun getOrderBook(@Query("book") book: String): Call<OrderBook>

    @GET("available_books")
    fun getAvailableBooks(): Call<AvailableBooksResponse>

}