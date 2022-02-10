package com.company;

import org.json.JSONObject;

import java.util.Date;

public class Forecast {
//    String API_URL = "http://api.openweathermap.org/data/2.5/weather";
    String API_URL = "https://api.weatherapi.com/v1/forecast.json?key=";

    public Date date;
    public Location location;

    public Forecast(Date date, Location location) {
        this.date = date;
        this.location = location;
    }

    public int getWeather() throws Exception {
        int finalResult = 0;
        JSONObject response = getWeatherResponse();
        int isRaining = response.getJSONObject("forecast").getJSONArray("forecastday")
                .getJSONObject(0).getJSONObject("day").getInt("daily_will_it_rain");
        int isSnowing = response.getJSONObject("forecast").getJSONArray("forecastday")
                .getJSONObject(0).getJSONObject("day").getInt("daily_will_it_snow");
        double temp = response.getJSONObject("forecast").getJSONArray("forecastday")
                .getJSONObject(0).getJSONObject("day").getDouble("avgtemp_c");
        int humidity = response.getJSONObject("forecast").getJSONArray("forecastday")
                .getJSONObject(0).getJSONObject("day").getInt("avghumidity");

        if(isRaining == 1){
            finalResult += WeatherTypes.RAIN.value;
        }
        if(isSnowing == 1){
            finalResult += WeatherTypes.SNOW.value;
        }

        double tempRatio = temp * humidity;
        if(tempRatio <= 900.0){
            finalResult += WeatherTypes.COLD.value;
        }else{
            finalResult += WeatherTypes.WARM.value;
        }

        return finalResult;
    }

    public String checkWeather(int weather) throws Exception {
        JSONObject response = getWeatherResponse(weather);
        int isRaining = response.getJSONObject("forecast").getJSONArray("forecastday")
                .getJSONObject(0).getJSONArray("hour").getJSONObject(0).getInt("will_it_rain");
        int isSnowing = response.getJSONObject("forecast").getJSONArray("forecastday")
                .getJSONObject(0).getJSONArray("hour").getJSONObject(0).getInt("will_it_snow");
        double temp = response.getJSONObject("forecast").getJSONArray("forecastday")
                .getJSONObject(0).getJSONArray("hour").getJSONObject(0).getDouble("temp_c");

        String weatherForecast = "Temperature will be: " + temp;

        if(isRaining == 1){
            weatherForecast += " There is a chance of rain.";
        }
        if(isSnowing == 1){
            weatherForecast += " There is a chance of snow.";
        }
        return weatherForecast;
    }

    private JSONObject getWeatherResponse() throws Exception {
//        String get_url = API_URL
//                + "?lat=" + location.latitude
//                + "&lon=" + location.longitude
//                + "&units=metric"
//                + "&appid=" + Main.OPEN_WEATHER_API_KEY;

        String get_url = API_URL
                + Main.OPEN_WEATHER_API_KEY
                + "&q=" + location.latitude
                + "," + location.longitude;
        return ApiConnector.getRequest(get_url);
    }

    private JSONObject getWeatherResponse(int hour) throws Exception {
        String get_url = API_URL
                + Main.OPEN_WEATHER_API_KEY
                + "&q=" + location.latitude
                + "," + location.longitude
                + "&hour=" + hour;
        return ApiConnector.getRequest(get_url);
    }
}
