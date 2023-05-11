/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.MotorPH;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.lang.reflect.Type;


/**
 *
 * @author Rolis
 */
public class AttendanceData {
    private String date;
    private String timeIn;
    private String timeOut;
    private String hoursRendered;
    private String present;
    private Attendance attendance = new Attendance();

    
    public AttendanceData(String date, String timeIn, String timeOut, String hoursRendered, String present) {
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.hoursRendered = hoursRendered;
        this.present = present;
    }

    public String getDate() {
        return date;
    }

    public String getTimeIn() {
        return timeIn;
    }
    
    public String getTimeOut() {
        return timeOut;
    }
    
    public String getHoursRendered() {
        return hoursRendered;
    }

    public String isPresent() {
        return present;
    }
}

