/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.MotorPH.UtilityClasses;

import com.google.gson.JsonArray;

/**
 *
 * @author Rolis
 */
public class NameIterator {
    
    private NameIterator() {
        throw new AssertionError();
    }
    
    public static String nameIterator(JsonArray json, String ENKey, String employeeNumber, String key) {
        for(int i = 0; i < json.size(); i++) {  // iterate through the JsonArray
            String result;
            // first I get the 'i' JsonElement as a JsonObject, then I get the key as a string and I compare it with the value
            if(json.get(i).getAsJsonObject().get(ENKey).getAsString().equals(employeeNumber)){
                result = json.get(i).getAsJsonObject().get(key).getAsString();
                return result;
            }

        }
    return null;
    }
    
    public static String nameIterator(String ENKey, String employeeNumber, String key, JsonArray json) {
        for(int i = 0; i < json.size(); i++) {  // iterate through the JsonArray
            String result;
            // first I get the 'i' JsonElement as a JsonObject, then I get the key as a string and I compare it with the value
            if (json.get(i).getAsJsonObject().get(ENKey).getAsString().equals(employeeNumber)){
                result = json.get(i).getAsJsonObject().get(key).getAsString();
                return result;
            }

        }
    return null;
    }
    
    public static String nameIterator(JsonArray json, String ENKey, String employeeNum,String userKey, String username, String key) {
        for(int i = 0; i < json.size(); i++) {  // iterate through the JsonArray
            String result;
            // first I get the 'i' JsonElement as a JsonObject, then I get the key as a string and I compare it with the value
            if(json.get(i).getAsJsonObject().get(userKey).getAsString().equals(username) && 
               json.get(i).getAsJsonObject().get(ENKey).getAsString().equals(employeeNum)){
                result = json.get(i).getAsJsonObject().get(key).getAsString();
                return result;
            }

        }
    return null;
    }
    
    public static String nameIterator(String ENKey, String employeeNum, String userKey, String username, String key, JsonArray json) {
        for(int i = 0; i < json.size(); i++) {  // iterate through the JsonArray
            String result;
            // first I get the 'i' JsonElement as a JsonObject, then I get the key as a string and I compare it with the value
            if(json.get(i).getAsJsonObject().get(userKey).getAsString().equals(username) && 
               json.get(i).getAsJsonObject().get(ENKey).getAsString().equals(employeeNum)){
                result = json.get(i).getAsJsonObject().get(key).getAsString();
                return result;
            }

        }
    return null;
    }
}
