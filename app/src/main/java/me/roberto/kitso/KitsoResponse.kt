package me.roberto.kitso

import android.support.annotation.Keep

/**
 * Created by roberto on 17/08/17.
 */

@Keep
class KitsoResponse<T>(var success: Boolean,var payload:T)