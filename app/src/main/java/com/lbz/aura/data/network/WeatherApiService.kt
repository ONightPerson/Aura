package com.lbz.aura.data.network

import com.lbz.aura.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    /**
     * 查询预报天气（extensions=all）
     */
    @GET("v3/weather/weatherInfo")
    suspend fun getForecastWeather(
        @Query("key") key: String,
        @Query("city") city: String,
        @Query("extensions") extensions: String = "all",
        @Query("output") output: String = "JSON"
    ): WeatherResponse

    /**
     * 查询实况天气（extensions=base）
     */
    @GET("v3/weather/weatherInfo")
    suspend fun getLiveWeather(
        @Query("key") key: String,
        @Query("city") city: String,
        @Query("extensions") extensions: String = "base",
        @Query("output") output: String = "JSON"
    ): WeatherResponse
}
