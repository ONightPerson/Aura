package com.lbz.aura.data.repository

import com.lbz.aura.data.model.City

/**
 * 常用城市列表（含 adcode）
 */
object CityData {

    val cities = listOf(
        City("北京", "110000"),
        City("上海", "310000"),
        City("广州", "440100"),
        City("深圳", "440300"),
        City("杭州", "330100"),
        City("南京", "320100"),
        City("苏州", "320500"),
        City("成都", "510100"),
        City("重庆", "500000"),
        City("武汉", "420100"),
        City("西安", "610100"),
        City("天津", "120000"),
        City("长沙", "430100"),
        City("郑州", "410100"),
        City("合肥", "340100"),
        City("福州", "350100"),
        City("厦门", "350200"),
        City("青岛", "370200"),
        City("济南", "370100"),
        City("大连", "210200"),
        City("沈阳", "210100"),
        City("哈尔滨", "230100"),
        City("长春", "220100"),
        City("昆明", "530100"),
        City("贵阳", "520100"),
        City("南宁", "450100"),
        City("南昌", "360100"),
        City("太原", "140100"),
        City("石家庄", "130100"),
        City("兰州", "620100"),
        City("乌鲁木齐", "650100"),
        City("拉萨", "540100"),
        City("海口", "460100"),
        City("三亚", "460200"),
        City("宁波", "330200"),
        City("无锡", "320200"),
        City("佛山", "440600"),
        City("东莞", "441900"),
        City("珠海", "440400"),
        City("温州", "330300"),
        City("泉州", "350500"),
        City("烟台", "370600"),
        City("徐州", "320300"),
        City("洛阳", "410300"),
        City("呼和浩特", "150100"),
        City("银川", "640100"),
        City("西宁", "630100"),
        City("香港", "810000"),
        City("澳门", "820000"),
        City("台北", "710100")
    )

    val defaultCity = cities.first()

    fun search(keyword: String): List<City> {
        if (keyword.isBlank()) return cities
        return cities.filter { it.name.contains(keyword.trim()) }
    }
}
