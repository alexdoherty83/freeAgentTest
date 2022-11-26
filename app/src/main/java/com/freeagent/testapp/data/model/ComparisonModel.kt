package com.freeagent.testapp.data.model

import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap
import java.util.*

data class ComparisonModel(
    @SerializedName("base")
    val base: String?,
    @SerializedName("end_date")
    val endDate: String?,
    @SerializedName("rates")
    val rates: TreeMap<String, LinkedTreeMap<String, String>>?,
    @SerializedName("start_date")
    val startDate: String?,
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("timeseries")
    val timeseries: Boolean?
)