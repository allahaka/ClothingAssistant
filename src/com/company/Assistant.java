package com.company;

import java.util.Scanner;

public class Assistant {
    public String addLocation() {
        Location location = this.addLocationHelper();
        try {
            location.updateCoordinates();
            Main.LocationsList.add(location);
        }catch(Exception e){
            return e.toString();
        }
        return "";
    }

    public String wearToday() {
        return "";
    }

    public String wearTomorrow() {
        return "";
    }

    public String checkWeather() {
        return "";
    }

    public String plantTrip() {
        return "";
    }

    private Location addLocationHelper() {
        cleanConsole();
        System.out.print("Provide name of the location you want to save: ");
        String name = getStringInput();
        System.out.print("Provide city of your location: ");
        String city = getStringInput();
        System.out.print("Provide a country of the city: ");
        String country = getStringInput();
        System.out.print("Provide a region of the country that the city is located in: ");
        String region = getStringInput();

        return new Location(name, city, country, region);
    }

    private String getStringInput(){
        Scanner scan = new Scanner(System.in);
        return scan.next();
    }

    public void cleanConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
