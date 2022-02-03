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
        JSONObject mainObj = new JSONObject(response.getString("main"));
        JSONObject wind = new JSONObject(response.getString("wind"));

        switch (Range.from(weatherId)){
            case TWO_HUNDRED -> finalResult += WeatherTypes.THUNDERSTORM.value;
            case THREE_HUNDRED -> finalResult += WeatherTypes.DRIZZLE.value;
            case FIVE_HUNDRED -> finalResult += WeatherTypes.RAIN.value;
            case SIX_HUNDRED -> finalResult += WeatherTypes.SNOW.value;
            case SEVEN_HUNDRED -> finalResult += WeatherTypes.ATMOSPHERE.value;
            case EIGHT_HUNDRED -> finalResult += WeatherTypes.CLEAR.value;
            case EIGHT_HUNDRED_PLUS -> finalResult += WeatherTypes.CLOUDS.value;
        }

        double tempRatio = mainObj.getDouble("temp") * mainObj.getInt("humidity");
        if(tempRatio <= -600.0){
            finalResult += WeatherTypes.FREEZING.value;
        }else if(tempRatio <= 600.0){
            finalResult += WeatherTypes.COLD.value;
        }else if(tempRatio <= 2300.0){
            finalResult += WeatherTypes.WARM.value;
        }else{
            finalResult += WeatherTypes.HOT.value;
        }

        double windSpeed = wind.getDouble("speed");
        if(windSpeed <= 6.5){
            finalResult += WeatherTypes.NO_WIND.value;
        }else if(windSpeed <= 11){
            finalResult += WeatherTypes.WINDY.value;
        }else{
            finalResult += WeatherTypes.A_LOT_OF_WIND.value;
        }

        return finalResult;
    }
}
