package com.mycompany.MotorPH;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.*;

import java.io.IOException;
import java.time.Month;
import java.time.YearMonth;

/**
 * JavaFX App
 */
public class App extends Application {
    
    private static Scene scene;
    
    private static UserData dataModel = new UserData();
    
    public void setDataModel(String user, String pass) {
        dataModel.setData(user, pass);
    }

    @Override
    public void start(Stage stage) throws IOException {
        //Set scene
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login" + ".fxml"));
        Parent root = fxmlLoader.load();
        
        //Look of the primary scene
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml, String user, String pass, String numOfHours, String netPay, Month month) throws IOException {
        //Set new scene
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        
        //Recursive set
        scene.setRoot(root);
        if ("login".equals(fxml)) {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setWidth(843.0);
            stage.setHeight(502.0);
            stage.centerOnScreen();
            return;
        }
        
         // Pass the information to different controllers
        Object controller = fxmlLoader.getController();
        if (controller instanceof Dashboard) {
            Dashboard dashboard = (Dashboard) controller;
            dashboard.setDataModel(user, pass, month);
        } else if (controller instanceof EmployeeDetails) {
            EmployeeDetails employeeDetails = (EmployeeDetails) controller;
            employeeDetails.setDataModel(user, pass, numOfHours, netPay, month);
        } else if (controller instanceof Attendance) {
            Attendance attendance = (Attendance) controller;
            attendance.setDataModel(user, pass, month);
        }   else if (controller instanceof Payroll) {
            Payroll payroll = (Payroll) controller;
            payroll.setDataModel(user, pass, month);
        }
        
        //Look of the Scene
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setWidth(1181.0);
        stage.setHeight(719.0);
        stage.centerOnScreen();
    }
    
    /*
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }*/

    public static void main(String[] args) {
        launch();
    }

}