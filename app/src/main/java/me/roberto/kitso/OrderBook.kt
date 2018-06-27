package me.roberto.kitso

import android.support.annotation.Keep

/**
 * Created by roberto on 12/11/17.
 */
@Keep
        class OrderBook(var asks:ArrayList<Order>,var bids:ArrayList<Order>,var updated:String,var sequence:Long)