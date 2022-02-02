package com.company;

import java.util.Scanner;

public class Menu {

    private final Assistant assistant = new Assistant();

    public void start(String msg) {
        int option = this.displayMenu(msg);

        this.runOption(option);
    }

    private int displayMenu(String msg){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        if(!(msg.equals("") || msg.equals(" "))){
            System.out.println(msg + "\n\n");
        }
        System.out.println("Welcome in clothing assistant");
        System.out.println("What would you like to do? Type the number corresponding to the option.");
        System.out.println("1) Add location");
        System.out.println("2) What should I wear now");
        System.out.println("3) What should I wear tomorrow");
        System.out.println("4) Check the weather at chosen location");
        System.out.println("5) What should i take with to going to location for the trip");
        System.out.println("\n6) Exit");
        System.out.print("Choose the option: ");
        return this.getInput();
    }

    private int getInput(){
        Scanner scan = new Scanner(System.in);
        return scan.nextInt();
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
                result = this.assistant.plantTrip();
                start(result);
            }
            case 6 -> System.exit(0);
        }
    }
}
