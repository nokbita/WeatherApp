参考：https://docs.caiyunapp.com/weather-api/v2/v2.6/

# 获取城市经纬度API

https://api.caiyunapp.com/v2/place?query=${place}&token=${TOKEN_CAIYUN}&lang=${language}

1. token ： 彩云天气的Token
2. query : 地点
3. lang : 语言

## 响应

> 测试数据
> 
> query=北京&token=xxx&lang=zh_CN

```json
{
  "status": "ok",
  "query": "北京",
  "places": [
    {
      "formatted_address": "中国 北京市 北京",
      "id": "CY_CN_d9b61a",
      "place_id": "CY_CN_d9b61a",
      "name": "中国 北京市 北京",
      "location": {
        "lat": 39.904989,
        "lng": 116.405285
      }
    },
    {
      "formatted_address": "中国 北京市 延庆",
      "id": "CY_CN_d95423",
      "place_id": "CY_CN_d95423",
      "name": "中国 北京市 延庆",
      "location": {
        "lat": 40.465325,
        "lng": 115.985006
      }
    }
  ]
}
```

# 实况API

https://api.caiyunapp.com/${TOKEN_CAIYUN}/${X},${Y}/realtime

根据经纬度，确定查询的位置。

1. {TOKEN_CAIYUN} ： 彩云天气的Token
2. {X} ： X坐标，可能是纬度
3. {Y} ： Y坐标，可能是经度

## 响应

> 测试数据
> 
> 101.6656,39.2072

```json
{
  "status": "ok",             // 返回状态  
  "api_version": "v2.6",    // API 版本
  "api_status": "active",     // API 服务状态
  "lang": "zh_CN",          // 返回语言
  "unit": "metric",        // 单位
  "tzshift": 28800,       // 时区偏移
  "timezone": "Asia/Shanghai",  // 时区
  "server_time": 1640745758,
  "location": [39.2072, 101.6656],
  "result": {
    "realtime": {
      "status": "ok",
      "temperature": -7,  // 地表 2 米气温
      "humidity": 0.58,  // 地表 2 米湿度相对湿度(%)
      "cloudrate": 0,  // 总云量(0.0-1.0)
      "skycon": "CLEAR_DAY",  // 天气现象
      "visibility": 7.8,  // 地表水平能见度
      "dswrf": 47.7,  // 向下短波辐射通量(W/M2)
      "wind": {
        "speed": 1.8,  // 地表 10 米风速
        "direction": 22  // 地表 10 米风向
      },
      "pressure": 85583.47,  // 地面气压
      "apparent_temperature": -9.9,  // 体感温度
      "precipitation": {
        "local": {
          "status": "ok",
          "datasource": "radar",
          "intensity": 0  // 本地降水强度
        },
        "nearest": {
          "status": "ok",
          "distance": 10000,  // 最近降水带与本地的距离
          "intensity": 0  // 最近降水处的降水强度
        }
      },
      "air_quality": {
        "pm25": 45,  // PM25 浓度(μg/m3)
        "pm10": 49,  // PM10 浓度(μg/m3)
        "o3": 6,  // 臭氧浓度(μg/m3)
        "so2": 8,  // 二氧化硫浓度(μg/m3)
        "no2": 42,  // 二氧化氮浓度(μg/m3)
        "co": 1.1,  // 一氧化碳浓度(mg/m3)
        "aqi": {
          "chn": 63,  // 国标 AQI
          "usa": 124
        },
        "description": {
          "chn": "良",
          "usa": "轻度污染"
        }
      },
      "life_index": {
        "ultraviolet": {
          "index": 3,
          "desc": "弱"  // 参见 [生活指数](tables/lifeindex)
        },
        "comfort": {
          "index": 12,
          "desc": "湿冷"  // 参见 [生活指数](tables/lifeindex)
        }
      }
    },
    "primary": 0
  }
}
```

# 预报-天气级API

https://api.caiyunapp.com/v2.6/${TOKEN_CAIYUN}/${X},${Y}/daily?dailysteps=${num}

1. dailysteps ： 控制返回多少天的数据，范围为[1, 15]。即：1表示查询未来一天的天气，3表示查询未来3天的天气。

## 响应

> 测试数据
> 
> 101.6656,39.2072
> 
> dailysteps=1

```json
{
  "status": "ok", // 返回状态
  "api_version": "v2.6", // API 版本
  "api_status": "alpha", // API 状态
  "lang": "zh_CN", // 语言
  "unit": "metric", // 单位
  "tzshift": 28800, // 时区偏移
  "timezone": "Asia/Shanghai", // 时区
  "server_time": 1653552787, // 服务器时间
  "location": [
    39.2072, // 纬度
    101.6656 // 经度
  ],
  "result": {
    "daily": {
      "status": "ok",
      "astro": [ // 日出日落时间
        {
          "date": "2022-05-26T00:00+08:00",
          "sunrise": {
            "time": "05:51" // 日出时间
          },
          "sunset": {
            "time": "20:28" // 日落时间
          }
        }
      ],
      "precipitation_08h_20h": [ // 白天降水数据
        {
          "date": "2022-05-26T00:00+08:00",
          "max": 0, // 白天最大降水量
          "min": 0, // 白天最小降水量
          "avg": 0, // 白天平均降水量
          "probability": 0 // 白天降水概率
        }
      ],
      "precipitation_20h_32h": [ // 夜晚降水数据
        {
          "date": "2022-05-26T00:00+08:00",
          "max": 0, // 夜晚最大降水量
          "min": 0, // 夜晚最小降水量
          "avg": 0, // 夜晚平均降水量
          "probability": 0 // 夜晚降水概率
        }
      ],
      "precipitation": [ // 降水数据
        {
          "date": "2022-05-26T00:00+08:00",
          "max": 0, // 全天最大降水量
          "min": 0, // 全天最小降水量
          "avg": 0, // 全天平均降水量
          "probability": 0 // 全天降水概率
        }
      ],
      "temperature": [ // 全天地表 2 米气温
        {
          "date": "2022-05-26T00:00+08:00",
          "max": 27, // 全天最高气温
          "min": 18, // 全天最低气温
          "avg": 23.75 // 全天平均气温
        }
      ],
      "temperature_08h_20h": [ // 白天地表 2 米气温
        {
          "date": "2022-05-26T00:00+08:00",
          "max": 27, // 白天最高气温
          "min": 18, // 白天最低气温
          "avg": 24.57 // 白天平均气温
        }
      ],
      "temperature_20h_32h": [ // 夜晚地表 2 米气温
        {
          "date": "2022-05-26T00:00+08:00",
          "max": 24.8, // 夜晚最高气温
          "min": 18, // 夜晚最低气温
          "avg": 20.02 // 夜晚平均气温
        }
      ],
      "wind": [ // 全天地表 10 米风速
        {
          "date": "2022-05-26T00:00+08:00",
          "max": {
            "speed": 28.24,
            "direction": 122.62
          },
          "min": {
            "speed": 9,
            "direction": 104
          },
          "avg": {
            "speed": 21.61,
            "direction": 118.02
          }
        }
      ],
      "wind_08h_20h": [ // 白天地表 10 米风速
        {
          "date": "2022-05-26T00:00+08:00",
          "max": {
            "speed": 28.24,
            "direction": 122.62
          },
          "min": {
            "speed": 9,
            "direction": 104
          },
          "avg": {
            "speed": 22.74,
            "direction": 115.78
          }
        }
      ],
      "wind_20h_32h": [ // 夜晚地表 10 米风速
        {
          "date": "2022-05-26T00:00+08:00",
          "max": {
            "speed": 22.39,
            "direction": 97.46
          },
          "min": {
            "speed": 9.73,
            "direction": 125.93
          },
          "avg": {
            "speed": 16,
            "direction": 121.62
          }
        }
      ],
      "humidity": [ // 地表 2 米相对湿度(%)
        {
          "date": "2022-05-26T00:00+08:00",
          "max": 0.18,
          "min": 0.08,
          "avg": 0.09
        }
      ],
      "cloudrate": [ // 云量(0.0-1.0)
        {
          "date": "2022-05-26T00:00+08:00",
          "max": 1,
          "min": 0,
          "avg": 0.75
        }
      ],
      "pressure": [ // 地面气压
        {
          "date": "2022-05-26T00:00+08:00",
          "max": 84500.84,
          "min": 83940.84,
          "avg": 83991.97
        }
      ],
      "visibility": [ // 地表水平能见度
        {
          "date": "2022-05-26T00:00+08:00",
          "max": 25, // 最大能见度
          "min": 24.13, // 最小能见度
          "avg": 25 // 平均能见度
        }
      ],
      "dswrf": [ // 向下短波辐射通量(W/M2)
        {
          "date": "2022-05-26T00:00+08:00",
          "max": 741.9, // 最大辐射通量
          "min": 0, // 最小辐射通量
          "avg": 368.6 // 平均辐射通量
        }
      ],
      "air_quality": {
        "aqi": [
          {
            "date": "2022-05-26T00:00+08:00",
            "max": {
              "chn": 183, // 中国国标 AQI 最大值
              "usa": 160 // 美国国标 AQI 最大值
            },
            "avg": {
              "chn": 29, // 中国国标 AQI 平均值
              "usa": 57 // 美国国标 AQI 平均值
            },
            "min": {
              "chn": 20, // 中国国标 AQI 最小值
              "usa": 42 // 美国国标 AQI 最小值
            }
          }
        ],
        "pm25": [
          {
            "date": "2022-05-26T00:00+08:00",
            "max": 74, // PM2.5 浓度最大值
            "avg": 15, // PM2.5 浓度平均值
            "min": 10 // PM2.5 浓度最小值
          }
        ]
      },
      "skycon": [
        {
          "date": "2022-05-26T00:00+08:00",
          "value": "PARTLY_CLOUDY_DAY" // 全天主要天气现象
        }
      ],
      "skycon_08h_20h": [
        {
          "date": "2022-05-26T00:00+08:00",
          "value": "PARTLY_CLOUDY_DAY" // 白天主要天气现象
        }
      ],
      "skycon_20h_32h": [
        {
          "date": "2022-05-26T00:00+08:00",
          "value": "CLOUDY" // 夜晚主要天气现象
        }
      ],
      "life_index": {
        "ultraviolet": [
          {
            "date": "2022-05-26T00:00+08:00",
            "index": "1",
            "desc": "最弱" // 紫外线指数自然语言
          }
        ],
        "carWashing": [
          {
            "date": "2022-05-26T00:00+08:00",
            "index": "1",
            "desc": "适宜" // 洗车指数自然语言
          }
        ],
        "dressing": [
          {
            "date": "2022-05-26T00:00+08:00",
            "index": "4",
            "desc": "温暖" // 穿衣指数自然语言
          }
        ],
        "comfort": [
          {
            "date": "2022-05-26T00:00+08:00",
            "index": "4",
            "desc": "温暖" // 舒适度指数自然语言
          }
        ],
        "coldRisk": [
          {
            "date": "2022-05-26T00:00+08:00",
            "index": "4",
            "desc": "极易发" // 感冒指数自然语言
          }
        ]
      }
    },
    "primary": 0
  }
}
```
