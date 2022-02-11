package com.company;

import org.json.JSONArray;

import java.util.ArrayList;

import static com.company.Assistant.intToBinary;

public class Wardrobe {
    // List of all clothes as:
    // [String name, int weatherTypes, String type]
    // weatherTypes:    Sum of correct weathers for clothes
    //    RAIN - 1,
    //    SNOW - 2,
    //    COLD - 4,
    //    WARM - 8,
    // types:
    //     neutral,
    //     casual,
    //     official,
    //     trip
    private static final String CLOTHES = """
            [
            ["beanie", 7, "neutral"],
            ["coat", 7, "neutral"],
            ["boot", 7, "neutral"],
            ["jacket", 7, "neutral"],
            ["scarf", 7, "neutral"],
            ["gloves", 6, "neutral"],
            ["hoodie", 7, "casual"],
            ["shoes", 8, "casual"],
            ["t-shirt", 8, "casual"],
            ["shirt", 15, "official"],
            ["suit", 15, "official"],
            ["jumper", 7, "casual"],
            ["tracksuit", 9, "casual"],
            ["cap", 8, "casual"],
            ["swimsuit", 8, "trip"],
            ["shorts", 8, "casual"],
            ["jeans", 15, "neutral"],
            ["raincoat", 1, "casual"],
            ["umbrella", 1, "neutral"],
            ["flip flops", 8, "casual"],
            ]""";
    public static void generateListOfClothes(){
        JSONArray jsonArray = new JSONArray(CLOTHES);

        for(int i=0; i<jsonArray.length(); i++){
            JSONArray el = jsonArray.getJSONArray(i);
            Main.listOfClothes.add(new Clothes(el.getString(0), el.getInt(1), el.getString(2)));
        }
    }

    public static ArrayList<String> whatClothesToUse(int weather){
        ArrayList<String> possibleClothes = new ArrayList<>();
        String weatherBinary = intToBinary(weather, 4);
        for(Clothes c: Main.listOfClothes){
            String cBinary = intToBinary(c.usedWeathers, 4);
            for(int i=0; i<4; i++) {
                char clothValue = cBinary.charAt(i);
                char weatherValue = weatherBinary.charAt(i);
                if( clothValue == '1' && weatherValue == clothValue){
                    possibleClothes.add(c.name);
                    break;
                }
            }
        }
        return possibleClothes;
    }
}
