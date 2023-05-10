package com.mycompany.MotorPH;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mycompany.MotorPH.UtilityClasses.Constants;
import com.mycompany.MotorPH.UtilityClasses.JsonFiles;
import com.mycompany.MotorPH.UtilityClasses.NameIterator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author Rolis
 */
public class Employee_details implements Initializable {

    @FXML
    private Label userName;
    @FXML
    private Label currentTime;
    @FXML
    private Label currentDate;
    @FXML
    private AnchorPane userTab;
    @FXML
    private Label fullName;
    
    private DataModel dataModel = new DataModel();
    
    private Dashboard dashboard = new Dashboard();
    @FXML
    private Label employeeNum;
    @FXML
    private Label birthday;
    @FXML
    private Label address;
    @FXML
    private Label phoneNumber;
    @FXML
    private Label sss;
    @FXML
    private Label Philhealth;
    @FXML
    private Label tin;
    @FXML
    private Label pagibig;
    @FXML
    private Label status;
    @FXML
    private Label position;
    @FXML
    private Label immediateSupervisor;
    @FXML
    private Label hourly_rate;
    
    private Month currentMonth;
    @FXML
    private Label numOfHours;
    @FXML
    private Label numOfDays;
    @FXML
    private Label netPay;
    
    public void setDataModel(String user, String pass, String numOfHours, String netPay, Month month) throws IOException{
        dataModel.setData(user, pass);
        currentMonth = month;
        setCardsInfo(numOfHours, netPay);
        setData();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Constants.setCurrentTime(currentTime);
        Constants.setCurrentDate(currentDate);
    }

    private void setData() throws IOException{
        String user = dataModel.getUser();
        String employeeNum = dataModel.getPass();
        
        //Set last name and first name
        this.employeeNum.setText(employeeNum);
        userName.setText(user);
        
        setInfo();
    }

    private void setCardsInfo(String numOfHours, String netPay) throws IOException{
        this.numOfHours.setText(numOfHours);
        this.netPay.setText(netPay);
    }
    
    private void setInfo() throws IOException{
        JsonArray json = JsonFiles.getEmployeesJSON();
        
        String user = dataModel.getUser();
        String firstName = NameIterator.nameIterator("employeeNum", employeeNum.getText(), "last_name", userName.getText(), "first_name", json);
        fullName.setText(user + ", "+ firstName);
        
        String birthday = NameIterator.nameIterator(json, "employeeNum", employeeNum.getText(), "last_name", userName.getText(), "birthday");
        this.birthday.setText(birthday);
        
        String address = NameIterator.nameIterator(json, "employeeNum", employeeNum.getText(), "last_name", userName.getText(), "address");
        this.address.setText(address);
        
        String phone_number = NameIterator.nameIterator(json, "employeeNum", employeeNum.getText(), "last_name", userName.getText(), "phone_number");
        phoneNumber.setText(phone_number);
        
        String sss = NameIterator.nameIterator(json, "employeeNum", employeeNum.getText(), "last_name", userName.getText(), "SSS");
        this.sss.setText(sss);
        
        String Philhealth = NameIterator.nameIterator(json, "employeeNum", employeeNum.getText(), "last_name", userName.getText(), "Philhealth");
        this.Philhealth.setText(Philhealth);
        
        String tin = NameIterator.nameIterator(json, "employeeNum", employeeNum.getText(), "last_name", userName.getText(), "TIN");
        this.tin.setText(tin);
        
        String pagibig = NameIterator.nameIterator(json, "employeeNum", employeeNum.getText(), "last_name", userName.getText(), "Pag-ibig");
        this.pagibig.setText(pagibig);
        
        String status = NameIterator.nameIterator(json, "employeeNum", employeeNum.getText(), "last_name", userName.getText(), "Status");
        this.status.setText(status);
        
        String position = NameIterator.nameIterator(json, "employeeNum", employeeNum.getText(), "last_name", userName.getText(), "Position");
        this.position.setText(position);
        
        String immediateSupervisor = NameIterator.nameIterator(json, "employeeNum", employeeNum.getText(), "last_name", userName.getText(), "immediate_supervisor");
        this.immediateSupervisor.setText(immediateSupervisor);
        
        String hourly_rate = NameIterator.nameIterator(json, "employeeNum", employeeNum.getText(), "last_name", userName.getText(), "hourly_rate");
        this.hourly_rate.setText(hourly_rate);
    }

    @FXML
    private void userHead(MouseEvent event) {
        if (userTab.isVisible()) {
            userTab.setVisible(false);
            return;
        }
        userTab.setVisible(true);
    }

    @FXML
    private void Logout(ActionEvent event) throws IOException {
        App.setRoot("login", null, null, null, null, null);
    }

    @FXML
    private void goToDashboard(ActionEvent event) throws IOException {
        App.setRoot("dashboard", userName.getText(), employeeNum.getText(), null, null, currentMonth);
    }

    @FXML
    private void goToAttendance(ActionEvent event) throws IOException {
        App.setRoot("attendance", userName.getText(), employeeNum.getText(), null, null, currentMonth);
    }

    @FXML
    private void goToPayroll(ActionEvent event) throws IOException {
        App.setRoot("payroll", userName.getText(), employeeNum.getText(), null, null,  currentMonth);
    }
    
}
