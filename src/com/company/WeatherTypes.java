package com.company;

public enum WeatherTypes {
    RAIN(1),
    SNOW(2),
    COLD(4),
    WARM(8);

    public final int value;

    WeatherTypes(int value){
        this.value = value;
    }

}
