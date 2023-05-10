package com.mycompany.MotorPH;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mycompany.MotorPH.UtilityClasses.JsonFiles;
import java.time.Month;
import java.time.YearMonth;

public class LoginPage {

    @FXML
    private TextField userField;
    @FXML
    private PasswordField passField;
    @FXML
    private Button LoginButton;
    @FXML
    private Label wrongLogin;
    
    private DataModel dataModel = new DataModel();
    
    private Month currentMonth = YearMonth.now().getMonth();


    @FXML
    private void onExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void onLogin(ActionEvent event) throws IOException{
        String user = userField.getText();
        String pass = passField.getText();
        JsonArray json = JsonFiles.getEmployeesJSON();
        
        if(!login(json, "last_name", user, "employeeNum", pass)){
            wrongLogin.setText("Wrong Information.");
            return;
        }
        App.setRoot("dashboard", user, pass, null, null, currentMonth);
    }
    
    public boolean login(JsonArray json, String userKey, String username, String passKey, String password) {
    for(int i = 0; i < json.size(); i++) {  // iterate through the JsonArray
        // first I get the 'i' JsonElement as a JsonObject, then I get the key as a string and I compare it with the value
        if(json.get(i).getAsJsonObject().get(userKey).getAsString().equals(username) &&
           json.get(i).getAsJsonObject().get(passKey).getAsString().equals(password)) 
            return true;
    }
    return false;
    }

}
