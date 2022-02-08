package com.company;

import org.json.JSONObject;

public class Location {
    String API_URL = "https://app.geocodeapi.io/api/v1/search?apikey=" + Main.GEO_DECODE_API_KEY + "&text=";

    public String name;
    public String country;
    public String city;
    public String region;
    public double latitude;
    public double longitude;

    public Location(String name, String country, String city, String region, double latitude, double longitude){
        this.name = name;
        this.country = country;
        this.city = city;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(String name, String country, String city, String region){
        this.name = name;
        this.country = country;
        this.city = city;
        this.region = region;
    }

    public void updateCoordinates() throws Exception {
        JSONObject response = ApiConnector.getRequest(API_URL + city + ", " + country + ", " + region);
        response = response.getJSONArray("features").getJSONObject(0);
        JSONObject geometry = response.getJSONObject("geometry");

        this.latitude = geometry.getJSONArray("coordinates").getDouble(1);
        this.longitude = geometry.getJSONArray("coordinates").getDouble(0);
    }

    public JSONObject getJSONObject(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", this.name);
        jsonObject.put("country", this.country);
        jsonObject.put("city", this.city);
        jsonObject.put("region", this.region);
        jsonObject.put("latitude", this.latitude);
        jsonObject.put("longitude", this.longitude);
        return jsonObject;
    }
}
