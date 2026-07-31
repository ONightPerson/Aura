package com.lbz.aura.data.model

import com.google.gson.annotations.SerializedName

/**
 * 高德天气 API 响应根对象
 */
data class WeatherResponse(
    @SerializedName("status") val status: String,
    @SerializedName("count") val count: String? = null,
    @SerializedName("info") val info: String? = null,
    @SerializedName("infocode") val infocode: String? = null,
    @SerializedName("lives") val lives: List<Live>? = null,
    @SerializedName("forecasts") val forecasts: List<Forecast>? = null
)

/**
 * 实况天气
 */
data class Live(
    @SerializedName("province") val province: String,
    @SerializedName("city") val city: String,
    @SerializedName("adcode") val adcode: String,
    @SerializedName("weather") val weather: String,
    @SerializedName("temperature") val temperature: String,
    @SerializedName("winddirection") val winddirection: String,
    @SerializedName("windpower") val windpower: String,
    @SerializedName("humidity") val humidity: String,
    @SerializedName("reporttime") val reporttime: String
)

/**
 * 预报天气
 */
data class Forecast(
    @SerializedName("city") val city: String,
    @SerializedName("adcode") val adcode: String,
    @SerializedName("province") val province: String,
    @SerializedName("reporttime") val reporttime: String,
    @SerializedName("casts") val casts: List<Cast>
)

/**
 * 单日预报
 */
data class Cast(
    @SerializedName("date") val date: String,
    @SerializedName("week") val week: String,
    @SerializedName("dayweather") val dayweather: String,
    @SerializedName("nightweather") val nightweather: String,
    @SerializedName("daytemp") val daytemp: String,
    @SerializedName("nighttemp") val nighttemp: String,
    @SerializedName("daywind") val daywind: String,
    @SerializedName("nightwind") val nightwind: String,
    @SerializedName("daypower") val daypower: String,
    @SerializedName("nightpower") val nightpower: String
)

/**
 * 城市数据（城市名 + adcode）
 */
data class City(
    val name: String,
    val adcode: String
)
