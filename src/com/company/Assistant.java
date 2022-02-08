package com.company;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static com.company.OsCheck.getOSType;

public class Assistant {

    private static final String MACOS_FILE_LOCATION = "/Users/Shared/ClothingAssistant.json";
    private static final String WINDOWS_FILE_LOCATION = System.getenv("APPDATA") + "\\ClothingAssistant.json";

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
            System.out.println("Before continuing you have to provide at least 5 locations \n");
            System.out.println("Firstly you have to set your home address");
            name = "home";
        }else if(!Menu.hasWorkLocation){
            System.out.println("Secondly you have to set your work address");
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

    public String saveLocations() {
        OsCheck.OSType osType = getOSType();
        if(osType == OsCheck.OSType.MacOs){
            return saveLocationsHelper(MACOS_FILE_LOCATION);
        }else if(osType == OsCheck.OSType.Windows){
            // Windows hasn't been tested
            return saveLocationsHelper(WINDOWS_FILE_LOCATION);
        }else{
            return "Currently this operating system is not supported.";
        }
    }

    public String saveLocationsHelper(String path){
        try{
            File saveFile = new File(path);
            if(!saveFile.exists()){
                saveFile.createNewFile();
            }

            FileWriter file = new FileWriter(path);

            file.write(createSaveContent());
            file.close();
        }catch(IOException e){
            return "Couldn't save you locations to file try to run program with escalated permissions";
        }
        return "";
    }

    public String createSaveContent(){
        JSONArray jsonArray = new JSONArray();
        for(Location l: Main.LocationsList){
            jsonArray.put(l.getJSONObject());
        }
        return jsonArray.toString();
    }

    public static List<Location> loadLocations(){
        List<Location> LocationsList = new ArrayList<>();
        OsCheck.OSType osType = getOSType();

        if(osType == OsCheck.OSType.MacOs){
            return loadLocationsHelper(MACOS_FILE_LOCATION);
        }else if(osType == OsCheck.OSType.Windows){
            // Windows hasn't been tested
            return loadLocationsHelper(WINDOWS_FILE_LOCATION);
        }

        return LocationsList;
    }

    public static List<Location> loadLocationsHelper(String path){
        List<Location> LocationsList = new ArrayList<>();

        try {
            File file = new File(path);
            String content = FileUtils.readFileToString(file, "utf-8");
            if (!file.exists()) {
                return LocationsList;
            }
            if (content == null) {
                return LocationsList;
            }

            JSONArray array = new JSONArray(content);

            for (int i = 0; i < array.length(); i++) {
                LocationsList.add(createLocationFromJson(array.getJSONObject(i)));
            }
            return LocationsList;
        }catch(Exception e){
            return LocationsList;
        }
    }

    public static Location createLocationFromJson(JSONObject obj){
        return new Location(
                obj.getString("name"),
                obj.getString("city"),
                obj.getString("country"),
                obj.getString("region"),
                obj.getDouble("latitude"),
                obj.getDouble("longitude"));
    }
}
