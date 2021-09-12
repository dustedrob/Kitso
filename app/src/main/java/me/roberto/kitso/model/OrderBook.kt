package me.roberto.kitso.model

import androidx.annotation.Keep

/**
 * Created by roberto on 12/11/17.
 */
@Keep
class OrderBook(val asks:ArrayList<Order>,
                val bids:ArrayList<Order>,
                val updated:String,
                val sequence:Long)