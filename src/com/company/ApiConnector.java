package com.company;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiConnector {
    public static JSONObject getRequest(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        if(!(responseCode == 200)){
            //TODO: Should be less generic
            throw new Exception("Something went wrong try again. ");
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while((inputLine = in.readLine()) != null){
            response.append(inputLine);
        }
        in.close();
        if(response.isEmpty()
            || response.toString().equals("{\"data\":[[]]}")
            || response.toString().equals("{}")){
            throw new Exception("API responded with empty data. ");
        }
        return new JSONObject(response.toString());
    }
}
