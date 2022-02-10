package com.company;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static com.company.OsCheck.getOSType;

public class Assistant {

    private static final String MACOS_FILE_LOCATION = "/Users/Shared/ClothingAssistant.json";
    private static final String WINDOWS_FILE_LOCATION = System.getenv("APPDATA") + "\\ClothingAssistant.json";

    public String addLocation() {
        Location location = this.addLocationHelper();
        try {
            location.updateCoordinates();
            addLocationToMain(location);
        }catch(Exception e){
            return e + "Try again in few seconds";
        }
        return "";
    }

    public String wearToday() {
        Forecast forecast = new Forecast(new Date(), Main.homeLocation);
        try{
            int weather = forecast.getWeather();
            ArrayList<String> possibilities = Wardrobe.whatClothesToUse(weather);
            return "You might consider to wear some of this options:\n" + possibleClothesToString(possibilities);
        }catch (Exception e){
            return e + "Try again in few seconds";
        }
    }

    public String wearTomorrow() {
        return "";
    }

    public String checkWeather(Location location, int weather) {
        try{
            Forecast forecast = new Forecast(new Date(), location);

            return forecast.checkWeather(weather);
        }catch(Exception e){
            return e + "Try again later";
        }
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

    public static void loadLocations(){
        OsCheck.OSType osType = getOSType();

        if(osType == OsCheck.OSType.MacOs){
            loadLocationsHelper(MACOS_FILE_LOCATION);
        }else if(osType == OsCheck.OSType.Windows){
            // Windows hasn't been tested
            loadLocationsHelper(WINDOWS_FILE_LOCATION);
        }
    }

    public static void loadLocationsHelper(String path){
        try {
            File file = new File(path);
            String content = FileUtils.readFileToString(file, "utf-8");
            if (!file.exists()) {
                return;
            }
            if (content == null) {
                return;
            }

            JSONArray array = new JSONArray(content);

            for (int i = 0; i < array.length(); i++) {
                Location location = createLocationFromJson(array.getJSONObject(i));
                addLocationToMain(location);
            }
        }catch(Exception ignored){
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

    public static void addLocationToMain(Location location){
        Main.LocationsList.add(location);
        if(location.name.equals("home")){
            Main.homeLocation = location;
        }
        if(location.name.equals("work")){
            Main.workLocation = location;
        }
    }


    private String possibleClothesToString(ArrayList<String> possibilities) {
        HashSet<String> uniqueValues = new HashSet<>(possibilities);
        return String.join(", ", uniqueValues);
    }

    public static String intToBinary(int number, int amountOfBits){
        StringBuilder binary = new StringBuilder();
        for(int i=0; i<amountOfBits; i++, number/=2){
            switch (number % 2) {
                case 0 -> binary.append("0");
                case 1 -> binary.append("1");
            }
        }
        return binary.reverse().toString();
    }
}
