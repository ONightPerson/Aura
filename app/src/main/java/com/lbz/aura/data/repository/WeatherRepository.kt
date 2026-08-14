package com.lbz.aura.data.repository

import com.lbz.aura.data.model.Forecast
import com.lbz.aura.data.network.RetrofitClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository {

    private val apiService = RetrofitClient.weatherApiService

    companion object {
        const val API_KEY = "0a0aebfb449b8c337d0920d96532a6ac"
    }

    /**
     * 获取预报天气（今天 + 未来3天）
     * 通过 withContext(Dispatchers.IO) 切换到子线程执行网络请求
     */
    suspend fun getForecastWeather(adcode: String): Result<Forecast> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getForecastWeather(key = API_KEY, city = adcode)
                val forecast = response.forecasts?.firstOrNull()
                if (response.status == "1" && forecast != null) {
                    Result.success(forecast)
                } else {
                    Result.failure(Exception("获取天气失败: ${response.info ?: "未知错误"}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * 获取实况天气
     * 通过 withContext(Dispatchers.IO) 切换到子线程执行网络请求
     */
    suspend fun getLiveWeather(adcode: String) = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getLiveWeather(key = API_KEY, city = adcode)
            val live = response.lives?.firstOrNull()
            if (response.status == "1" && live != null) {
                Result.success(live)
            } else {
                Result.failure(Exception("获取实况天气失败: ${response.info ?: "未知错误"}"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
