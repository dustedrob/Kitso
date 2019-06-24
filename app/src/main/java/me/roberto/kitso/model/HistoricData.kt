package me.roberto.kitso.model

import androidx.annotation.Keep

/**
 * Created by roberto on 12/11/17.
 */
@Keep
class HistoricData(var date:String,var dated:String,var value:String,var volume:String,
                   var open:String,var low:String,var high:String, var close: String, var vwap:String)