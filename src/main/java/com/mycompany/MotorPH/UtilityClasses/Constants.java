/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.MotorPH.UtilityClasses;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 *
 * @author Rolis
 */
public class Constants {
    
    private Constants() {
        throw new AssertionError();
    }
    
    public static void setCurrentTime(Label time){
        // Set current time to label currentTime
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {        
            LocalTime currentTimeValue = LocalTime.now();
            time.setText(currentTimeValue.format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
    
    public static void setCurrentDate(Label date){
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        date.setText(LocalDate.now().format(dateFormat));
    }
    
    public static void setChoiceBoxValues(ChoiceBox<Month> month, Month currentMonth){
        month.getItems().addAll(Month.values());
        month.setValue(currentMonth);
    }
    
}
