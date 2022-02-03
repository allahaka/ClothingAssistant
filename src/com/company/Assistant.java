package com.company;

import java.util.Date;
import java.util.Scanner;

public class Assistant {
    public String addLocation() {
        Location location = this.addLocationHelper();
        try {
            location.updateCoordinates();
            Main.LocationsList.add(location);
            if(location.name.equals("home")){
                Main.homeLocation = location;
            }
            if(location.name.equals("work")){
                Main.workLocation = location;
            }
        }catch(Exception e){
            return e + "Try again in few seconds";
        }
        return "";
    }

    public String wearToday() {
        Forecast forecast = new Forecast(new Date(), Main.homeLocation);
        try{
            int weather = forecast.getWeather();
        }catch (Exception e){
            return e + "Try again in few seconds";
        }
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

        String name;
        if(!Menu.hasHomeLocation){
            System.out.println("Firstly you have to set your home address");
            name = "home";
        }else if(!Menu.hasWorkLocation){
            System.out.println("Firstly you have to set your work address");
            name = "work";
        }else{
            System.out.print("Provide name of the location you want to save: ");
            name = getStringInput();
        }

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
