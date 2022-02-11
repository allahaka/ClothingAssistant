package com.company;

public class Menu {

    private final Assistant assistant = new Assistant();
    private boolean minimumLocationNumber = false;
    public static boolean hasHomeLocation = false;
    public static boolean hasWorkLocation = false;

    public void start(String msg){
        if(!minimumLocationNumber){
            while(Main.LocationsList.size() < 5){
                if(!hasWorkLocation || !hasHomeLocation){
                    for (Location l : Main.LocationsList){
                        if (l.name.equalsIgnoreCase("home")){
                            hasHomeLocation = true;
                        }
                        if (l.name.equalsIgnoreCase("work")){
                            hasWorkLocation = true;
                        }
                    }
                }
                runOption(1);
            }
            minimumLocationNumber = true;
        }

        int option = this.displayMenu(msg);
        this.runOption(option);
    }

    private int displayMenu(String msg){
        assistant.cleanConsole();
        if(!(msg.equals("") || msg.equals(" "))){
            System.out.println(msg + "\n\n");
        }
        System.out.println("What would you like to do? Type the number corresponding to the option.");
        System.out.println("1) Add location");
        System.out.println("2) What should I wear now");
        System.out.println("3) What should I wear tomorrow to work");
        System.out.println("4) Check the weather at chosen location");
        System.out.println("5) What should i take with to going to location for the trip");
        System.out.println("6) Save locations");
        System.out.println("7) Exit");
        System.out.print("Choose the option: ");
        return Assistant.getInput();
    }

    private void runOption(int option){
        String result;
        switch(option){
            case 1 -> {
                result = this.assistant.addLocation();
                start(result);
            }
            case 2 -> {
                result = this.assistant.wearToday();
                start(result);
            }
            case 3 -> {
                result = this.assistant.wearTomorrow();
                start(result);
            }
            case 4 -> {
                result = this.assistant.checkWeather();
                start(result);
            }
            case 5 -> {
                result = this.assistant.planATrip();
                start(result);
            }
            case 6 -> {
                result = this.assistant.saveLocations();
                start(result);
            }
            case 7 -> System.exit(0);
            default -> {
                System.out.println("Incorrect Option");
                start("");
            }
        }
    }
}
