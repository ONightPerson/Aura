package com.lbz.aura.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lbz.aura.data.model.Cast
import com.lbz.aura.data.model.City
import com.lbz.aura.data.model.Live
import com.lbz.aura.data.repository.CityData
import com.lbz.aura.data.repository.WeatherRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 天气页面 UI 状态
 */
data class WeatherUiState(
    val isLoading: Boolean = false,
    val currentCity: City = CityData.defaultCity,
    val liveWeather: Live? = null,
    val forecastList: List<Cast> = emptyList(),
    val reportTime: String = "",
    val error: String? = null
)

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        loadWeather(CityData.defaultCity)
    }

    /**
     * 切换城市并加载天气
     */
    fun selectCity(city: City) {
        if (city.adcode == _uiState.value.currentCity.adcode) return
        loadWeather(city)
    }

    /**
     * 刷新当前城市天气
     */
    fun refresh() {
        loadWeather(_uiState.value.currentCity)
    }

    private fun loadWeather(city: City) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            currentCity = city,
            error = null
        )
        viewModelScope.launch {
            // 并行请求实况与预报（网络请求在 IO 子线程执行）
            val liveDeferred = async { repository.getLiveWeather(city.adcode) }
            val forecastDeferred = async { repository.getForecastWeather(city.adcode) }

            val liveResult = liveDeferred.await()
            val forecastResult = forecastDeferred.await()

            var live: Live? = null
            var casts: List<Cast> = emptyList()
            var reportTime = ""
            var errorMsg: String? = null

            liveResult.onSuccess { live = it }
                .onFailure { errorMsg = it.message }

            forecastResult.onSuccess { forecast ->
                casts = forecast.casts
                reportTime = forecast.reporttime
            }.onFailure { errorMsg = it.message }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                liveWeather = live,
                forecastList = casts,
                reportTime = reportTime,
                error = if (live == null && casts.isEmpty()) errorMsg else null
            )
        }
    }
}
