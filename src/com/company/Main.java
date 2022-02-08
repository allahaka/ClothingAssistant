package com.company;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static String GEO_DECODE_API_KEY;
    public static String OPEN_WEATHER_API_KEY;
    public static List<Location> LocationsList = new ArrayList<>();
    public static Location homeLocation;
    public static Location workLocation;

    public static void main(String @NotNull [] args){
        Main.GEO_DECODE_API_KEY = args[0];
        Main.OPEN_WEATHER_API_KEY = args[1];

        Main.LocationsList = Assistant.loadLocations();

        Menu menu = new Menu();
        menu.start("");
    }
}
