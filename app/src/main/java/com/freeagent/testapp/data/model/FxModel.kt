package com.freeagent.testapp.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class FxModel(
    @SerializedName("base")
    val base: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("rates")
    val rates: TreeMap<String, String>?,
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("timestamp")
    val timestamp: Int?
)