package com.gr.manchid.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyVar(
    var yturl: String = "",
    var price: Int = 0
): Parcelable
