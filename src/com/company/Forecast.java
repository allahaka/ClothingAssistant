package com.company;

import org.json.JSONObject;

public class Forecast {
    String API_URL = "https://api.weatherapi.com/v1/forecast.json?key=";

    public Location location;

    public Forecast(Location location) {
        this.location = location;
    }

    public int getWeather() throws Exception {
        JSONObject response = getWeatherResponse();
        int isRaining = response.getJSONObject("forecast").getJSONArray("forecastday")
                .getJSONObject(0).getJSONObject("day")
                .getInt("daily_will_it_rain");
        int isSnowing = response.getJSONObject("forecast").getJSONArray("forecastday")
                .getJSONObject(0).getJSONObject("day")
                .getInt("daily_will_it_snow");
        double temp = response.getJSONObject("forecast").getJSONArray("forecastday")
                .getJSONObject(0).getJSONObject("day")
                .getDouble("avgtemp_c");
        int humidity = response.getJSONObject("forecast").getJSONArray("forecastday")
                .getJSONObject(0).getJSONObject("day")
                .getInt("avghumidity");
        return getWeatherInt(isRaining, isSnowing, temp, humidity);
    }

    public int getWeather(String date, int hour) throws Exception {
        JSONObject response = getWeatherResponse(date, hour);
        int isRaining = response.getJSONObject("forecast").getJSONArray("forecastday")
                .getJSONObject(0).getJSONArray("hour")
                .getJSONObject(0).getInt("will_it_rain");
        int isSnowing = response.getJSONObject("forecast").getJSONArray("forecastday")
                .getJSONObject(0).getJSONArray("hour")
                .getJSONObject(0).getInt("will_it_snow");
        double temp = response.getJSONObject("forecast").getJSONArray("forecastday")
                .getJSONObject(0).getJSONArray("hour")
                .getJSONObject(0).getDouble("temp_c");
        int humidity = response.getJSONObject("forecast").getJSONArray("forecastday")
                .getJSONObject(0).getJSONArray("hour")
                .getJSONObject(0).getInt("humidity");
        return getWeatherInt(isRaining, isSnowing, temp, humidity);
    }

    public int getWeatherInt(int isRaining, int isSnowing, double temp, int humidity){
        int finalResult = 0;


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

    public String checkWeather(int time) throws Exception {
        JSONObject response = getWeatherResponse(time);
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

    private JSONObject getWeatherResponse(String date, int hour) throws Exception {
        String get_url = API_URL
                + Main.OPEN_WEATHER_API_KEY
                + "&q=" + location.latitude
                + "," + location.longitude
                + "&dt=" + date
                + "&hour=" + hour;
        return ApiConnector.getRequest(get_url);
    }
}
