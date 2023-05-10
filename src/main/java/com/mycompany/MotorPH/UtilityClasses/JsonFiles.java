package com.mycompany.MotorPH.UtilityClasses;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Rolis
 */
public final class JsonFiles {
    
    private static String attendanceJson = "./src/main/resources/JSON_Files/Attendance.json";
    
    private static String employeesJson = "./src/main/resources/JSON_Files/Employees.json";
    
    private JsonFiles() {
        throw new AssertionError();
    }
    
    public static String getAttendanceJsonPath(){
        return attendanceJson;
    }
    
    public static String getEmployeesJsonPath(){
        return employeesJson;
    }
    
    public static JsonArray getEmployeesJSON() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(getEmployeesJsonPath()));
        Gson gson = new Gson();
        JsonArray json = gson.fromJson(reader, JsonArray.class);
        return json;
    }
    
    public static JsonArray getAttendanceJSON() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(getAttendanceJsonPath()));
        Gson gson = new Gson();
        JsonArray json = gson.fromJson(reader, JsonArray.class);
        return json;
    }
    
    public static JsonArray getAttendanceJSON(String filePath) throws IOException {
        // Read the JSON file into a string
        String jsonStr = new String(Files.readAllBytes(Paths.get(filePath)));

        // Parse the string into a JsonArray
        JsonArray jsonArray = JsonParser.parseString(jsonStr).getAsJsonArray();

        return jsonArray;
    }
}
