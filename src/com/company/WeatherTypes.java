package com.company;

public enum WeatherTypes {
    THUNDERSTORM(1),
    DRIZZLE(2),
    RAIN(4),
    SNOW(8),
    ATMOSPHERE(16), // Mist, smoke, haze, sand, fog, sand, dust
    CLEAR(32),
    CLOUDS(64),
    FREEZING(128),
    COLD(256),
    WARM(512),
    HOT(1024),
    NO_WIND(2048),
    WINDY(4096),
    A_LOT_OF_WIND(8192);

    public final int value;

    WeatherTypes(int value){
        this.value = value;
    }

}
