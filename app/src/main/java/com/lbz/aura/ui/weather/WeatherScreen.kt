package com.lbz.aura.ui.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lbz.aura.data.model.Cast

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showCityPicker by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF4A90D9),
                        Color(0xFF67B8E3),
                        Color(0xFFA8D8EA)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 顶部城市栏
            TopCityBar(
                cityName = uiState.currentCity.name,
                reportTime = uiState.reportTime,
                onCityClick = { showCityPicker = true },
                onRefreshClick = { viewModel.refresh() }
            )

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }

                uiState.error != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "加载失败\n${uiState.error}",
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextButton(onClick = { viewModel.refresh() }) {
                            Text("点击重试", color = Color.White)
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 今日天气卡片
                        uiState.liveWeather?.let { live ->
                            item {
                                TodayWeatherCard(
                                    weather = live.weather,
                                    temperature = live.temperature,
                                    windDirection = live.winddirection,
                                    windPower = live.windpower,
                                    humidity = live.humidity
                                )
                            }
                        }

                        item {
                            Text(
                                text = "未来天气预报",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }

                        // 预报列表
                        items(uiState.forecastList) { cast ->
                            ForecastCard(cast = cast)
                        }
                    }
                }
            }
        }

        // 城市选择对话框
        if (showCityPicker) {
            CityPickerDialog(
                currentCity = uiState.currentCity,
                onCitySelected = { city ->
                    viewModel.selectCity(city)
                    showCityPicker = false
                },
                onDismiss = { showCityPicker = false }
            )
        }
    }
}

@Composable
private fun TopCityBar(
    cityName: String,
    reportTime: String,
    onCityClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = onCityClick)
            ) {
                Text(
                    text = cityName,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "切换城市",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            if (reportTime.isNotEmpty()) {
                Text(
                    text = "数据更新: $reportTime",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }
        Text(
            text = "刷新",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .clickable(onClick = onRefreshClick)
                .background(
                    Color.White.copy(alpha = 0.2f),
                    RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun TodayWeatherCard(
    weather: String,
    temperature: String,
    windDirection: String,
    windPower: String,
    humidity: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.25f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weather,
                color = Color.White,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${temperature}°",
                color = Color.White,
                fontSize = 72.sp,
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherInfoItem(label = "风向", value = windDirection)
                WeatherInfoItem(label = "风力", value = "${windPower}级")
                WeatherInfoItem(label = "湿度", value = "${humidity}%")
            }
        }
    }
}

@Composable
private fun WeatherInfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp
        )
    }
}

@Composable
private fun ForecastCard(cast: Cast) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 日期与星期
            Column(modifier = Modifier.width(80.dp)) {
                Text(
                    text = formatDate(cast.date),
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = weekDayText(cast.week),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 13.sp
                )
            }

            // 天气现象
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cast.dayweather,
                    color = Color.White,
                    fontSize = 15.sp
                )
                if (cast.dayweather != cast.nightweather) {
                    Text(
                        text = "夜间: ${cast.nightweather}",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }

            // 温度
            Text(
                text = "${cast.nighttemp}° ~ ${cast.daytemp}°",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun formatDate(date: String): String {
    // "2026-07-31" -> "07-31"
    val parts = date.split("-")
    return if (parts.size == 3) "${parts[1]}-${parts[2]}" else date
}

private fun weekDayText(week: String): String {
    return when (week) {
        "1" -> "周一"
        "2" -> "周二"
        "3" -> "周三"
        "4" -> "周四"
        "5" -> "周五"
        "6" -> "周六"
        "7" -> "周日"
        else -> "周$week"
    }
}
