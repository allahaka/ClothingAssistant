package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

public class Location {
    String API_URL = "http://api.positionstack.com/v1/forward?access_key=" + Main.GEO_DECODE_API_KEY + "&query=";

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
        JSONArray respArray = response.getJSONArray("data");
        this.latitude = respArray.getJSONObject(0).getDouble("latitude");
        this.longitude = respArray.getJSONObject(0).getDouble("longitude");
    }
}
