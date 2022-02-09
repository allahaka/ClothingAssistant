package com.company;

import org.json.JSONObject;

import java.util.Date;

public class Forecast {
    String API_URL = "http://api.openweathermap.org/data/2.5/weather";

    public Date date;
    public Location location;

    public Forecast(Date date, Location location) {
        this.date = date;
        this.location = location;
    }

    public int getWeather() throws Exception {
        int finalResult = 0;
        String get_url = API_URL
                + "?lat=" + location.latitude
                + "&lon=" + location.longitude
                + "&units=metric"
                + "&appid=" + Main.OPEN_WEATHER_API_KEY;
        JSONObject response = ApiConnector.getRequest(get_url);
        int weatherId = response.getJSONArray("weather").getJSONObject(0).getInt("id");
        JSONObject mainObj = response.getJSONObject("main");

        if(weatherId < 600){
            finalResult += WeatherTypes.RAIN.value;
        }else{
            finalResult += WeatherTypes.SNOW.value;
        }

        double tempRatio = mainObj.getDouble("temp") * mainObj.getInt("humidity");
        if(tempRatio <= 900.0){
            finalResult += WeatherTypes.COLD.value;
        }else{
            finalResult += WeatherTypes.WARM.value;
        }

        return finalResult;
    }
}
