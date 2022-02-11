package com.company;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
        Forecast forecast = new Forecast(Main.homeLocation);
        try{
            int weather = forecast.getWeather();
            ArrayList<String> possibilities = Wardrobe.whatClothesToUse(weather);
            return "You might consider to wear some of this options:\n" + possibleClothesToString(possibilities);
        }catch (Exception e){
            return e + "Try again in few seconds";
        }
    }

    public String wearTomorrow() {
        System.out.println("What time are you going to work: (24h format)");
        int goOutTime = Assistant.getInput();
        System.out.println("What time are you coming back: (24h format)");
        int goBackTime = Assistant.getInput();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try{
            String tomorrow = format.format(calendar.getTime());
            Forecast goOutForecast = new Forecast(Main.homeLocation);
            Forecast comeBackForecast = new Forecast(Main.workLocation);
            int weatherGoOut = goOutForecast.getWeather(tomorrow, goOutTime);
            int weatherComeBack = comeBackForecast.getWeather(tomorrow, goBackTime);
            ArrayList<String> possibleClothes = Wardrobe.whatClothesToUse(weatherGoOut);
            possibleClothes.addAll(Wardrobe.whatClothesToUse(weatherComeBack));
            return "You might consider to wear some of this options:\n" + possibleClothesToString(possibleClothes);
        }catch(Exception e){
            return e + "Try again later";
        }
    }

    public String checkWeather() {
        System.out.println("Provide the number of location: ");
        for(int i=0; i<Main.LocationsList.size(); i++){
            System.out.println(i + ") " + Main.LocationsList.get(i).toString());
        }
        int locationNumber = Assistant.getInput();
        Location location = Main.LocationsList.get(locationNumber);
        System.out.println("Provide what time you want to check: (24h system)");
        int time = Assistant.getInput();
        try{
            Forecast forecast = new Forecast(location);
            return forecast.checkWeather(time);
        }catch(Exception e){
            return e + "Try again later";
        }
    }

    public String planATrip() {
        System.out.println("Provide the number of location for your trip: ");
        for(int i=0; i<Main.LocationsList.size(); i++){
            System.out.println(i + ") " + Main.LocationsList.get(i).toString());
        }
        int locationNumber = Assistant.getInput();
        Location location = Main.LocationsList.get(locationNumber);
        System.out.println("Remember that we can only go forward maximum 15 days.");
        System.out.println("Try to fit in with your last day before 15 days from today.");
        System.out.println("What day you plan to go out? (Provide date in yyyy-mm-dd format)");
        String goDate = getStringInput();
        System.out.println("What day you plan to come back? (Provide date in yyyy-mm-dd format)");
        String comeBackDate = getStringInput();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        try{
            ArrayList<String> allClothes = new ArrayList<>();
            Forecast forecast = new Forecast(location);
            c.setTime(sdf.parse(goDate));
            Date d1 = sdf.parse(goDate);
            Date d2 = sdf.parse(comeBackDate);
            long diff = d2.getTime() - d1.getTime();
            int diffDays = (int)diff / (24 * 60 * 60 * 1000);
            for(int i=0; i<=diffDays; i++){
                c.add(Calendar.DAY_OF_WEEK, 1);
                int weather = forecast.getWeather(sdf.format(c.getTime()));
                allClothes.addAll(Wardrobe.whatClothesToUse(weather));
            }
            return "You might consider packing this clothes:\n" + possibleClothesToString(allClothes);
        }catch(Exception e){
            return e + "Try again";
        }
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

    private String possibleClothesToString(ArrayList<String> possibilities) {
        HashSet<String> uniqueValues = new HashSet<>(possibilities);
        return String.join(", ", uniqueValues);
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

    public static int getInput(){
        Scanner scan = new Scanner(System.in);
        return scan.nextInt();
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
