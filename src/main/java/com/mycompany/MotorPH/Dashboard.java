/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.MotorPH;


import com.mycompany.MotorPH.UtilityClasses.JsonFiles;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import javafx.util.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mycompany.MotorPH.UtilityClasses.Constants;
import com.mycompany.MotorPH.UtilityClasses.NameIterator;
import com.mycompany.MotorPH.UtilityClasses.SalaryCalculator;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.Month;
import static java.time.Month.*;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Iterator;
import javafx.scene.control.ChoiceBox;
import static org.apache.commons.math3.util.Precision.round;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import static org.apache.poi.ss.usermodel.CellType.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

/**
 * FXML Controller class
 *
 * @author Rolis
 */
public class Dashboard implements Initializable {

    @FXML
    private AnchorPane userTab;
    @FXML
    private Label currentTime;
    @FXML
    private Label currentDate;
    @FXML
    private Label userName;
    
    private DataModel dataModel = new DataModel();
    @FXML
    private Label lastName;
    @FXML
    private Label firstName;
    @FXML
    private Label employeeNumber;
    @FXML
    private Label employeePosition;
    @FXML
    private ChoiceBox<Month> attendanceChoose;
    
    private Month currentMonth; /*YearMonth.now().getMonth();*/
    @FXML
    public Label numOfHours;
    
    private final String firstNameKey = "first_name";
    
    private final String lastNameKey = "last_name";
    
    private final String ENKey = "employeeNum";
    @FXML
    private Label numOfDays;
    @FXML
    private Label monthChosen;
    @FXML
    public Label netPay;
    
    private String hourlyPay;
    

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Constants.setCurrentTime(currentTime);
        Constants.setCurrentDate(currentDate);
        /*setMonthChosenDefault();*/
        setMonthUpdates();
        attendanceChoose.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                setAttendanceValues(numOfHours, numOfDays);
                computeGrossPay(netPay);
                setPassableData();
            } catch (IOException ex) {
                Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    //**************************************************************************************************************************************
    //*******************************************Here is where we set everything on the Dashboard*******************************************
    //**************************************************************************************************************************************
    
    /*****************************
     * GENERAL
     *****************************/
    public void setDataModel(String user, String pass, Month month) throws IOException {
        dataModel.setData(user, pass);
        currentMonth = month;
        setData();
    }
    
    private void setData() throws IOException{
        String user = dataModel.getUser();
        String employeeNum = dataModel.getPass();
        
        //Set last name and first name
        userName.setText(user);
        employeeNumber.setText(employeeNum);
        
        setInfoCard();
        Constants.setChoiceBoxValues(attendanceChoose, currentMonth);
    }
    
    private void setPassableData() throws IOException{
        dataModel.setNetPay(this.netPay.getText());
        dataModel.setNumOfHours(this.numOfHours.getText());
    }
    
    public String getData() throws IOException{
        return dataModel.getUser();
    }
    
    public void setMonthUpdates(){
        attendanceChoose.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentMonth = newValue;
        });
    }
    
    /*****************************
     * INFORMATION CARD
     *****************************/
    
    public void setInfoCard() throws IOException{
        
        JsonArray json = JsonFiles.getEmployeesJSON();
        
        String fnResult = NameIterator.nameIterator(ENKey, employeeNumber.getText(), firstNameKey, json);
        firstName.setText(fnResult);
        
        String lnResult = NameIterator.nameIterator(json, ENKey, employeeNumber.getText(), lastNameKey);
        lastName.setText(lnResult);
        
        String position = NameIterator.nameIterator(json, ENKey, employeeNumber.getText(), "Position");
        employeePosition.setText(position);
        
        String hourlyPay = NameIterator.nameIterator(ENKey, employeeNumber.getText(), "hourly_rate", json);
        this.hourlyPay = hourlyPay;
    }
    
    /*****************************
     * ATTENDANCE CARD
     *****************************/
    
    public void setAttendanceValues(Label numOfHours, Label numOfDays) throws IOException{
        JsonArray json = JsonFiles.getAttendanceJSON();
        int numberOfElements = SalaryCalculator.getNumberOfElements(json, ENKey, employeeNumber.getText(), currentMonth);
        
        long[] attendance = SalaryCalculator.getAttendance(json, ENKey, employeeNumber.getText(), numberOfElements, currentMonth);
        
        long[] filteredAttendance = Arrays.stream(attendance).filter(num -> num != 0).toArray();    
        
        String sumOfAttendance = Long.toString(SalaryCalculator.getSumOfAttendance(attendance));
        
        numOfHours.setText(sumOfAttendance + " hours");
        numOfDays.setText(filteredAttendance.length + " days");
    }
    
    /*****************************
     * PAYROLL CARD
     *****************************/
    public void computeGrossPay(Label netPay) throws IOException{
        JsonArray jsonAttendance = JsonFiles.getAttendanceJSON();
        JsonArray jsonEmployees = JsonFiles.getEmployeesJSON();
        int numberOfElements = SalaryCalculator.getNumberOfElements(jsonAttendance, ENKey, employeeNumber.getText(), currentMonth);
        long[] attendance = SalaryCalculator.getAttendance(jsonAttendance, ENKey, employeeNumber.getText(), numberOfElements, currentMonth);
        String sumOfAttendance = Long.toString(SalaryCalculator.getSumOfAttendance(attendance));
        
        //Computation of Gross Salary
        double hourlyPay = Double.parseDouble(this.hourlyPay);
        double numOfHours = Double.parseDouble(sumOfAttendance);
        double grossSalary = hourlyPay * numOfHours;
        
        //Allowances
        double riceSubsidy = Double.parseDouble(NameIterator.nameIterator(jsonEmployees, ENKey, employeeNumber.getText(), "rice_subsidy"));
        double phoneAllowance = Double.parseDouble(NameIterator.nameIterator(jsonEmployees, ENKey, employeeNumber.getText(), "phone_allowance"));
        double clothingAllowance = Double.parseDouble(NameIterator.nameIterator(jsonEmployees, ENKey, employeeNumber.getText(), "clothing_allowance"));
        
        //Deductions Computation
        double philHealthDeduction = SalaryCalculator.getPhilHealth(grossSalary);
        double sssDeduction = SalaryCalculator.getSSS(grossSalary);
        double pagibigDeduction = SalaryCalculator.getPagibig(grossSalary);
        double totalDeductions = philHealthDeduction + sssDeduction + pagibigDeduction;
        
        //Salary after the deductions
        double initialSalary = grossSalary - totalDeductions;
        double withHoldingTax = SalaryCalculator.getWithholding(initialSalary);
        
        //Salary after tax
        double netSalary = initialSalary - withHoldingTax;
        
        if (netSalary != 0)
            netSalary = netSalary + riceSubsidy + phoneAllowance + clothingAllowance;
        
        //Set the formatting of Decimals
        DecimalFormat df = new DecimalFormat("#.##");
        
        //Set the value of netSalary to netPay
        this.netPay.setText("₱" + df.format(netSalary));
    }
    
    
    //*************************************************************************************************************************************
    //*******************************************End of where we set everything on the Dashboard*******************************************
    //*************************************************************************************************************************************

    /*****************************
     * EVENTS
     *****************************/
    
    @FXML
    private void toYourInfo(ActionEvent event) throws IOException {
        App.setRoot("employee_details", userName.getText(), employeeNumber.getText(), numOfHours.getText(), netPay.getText(), currentMonth);
    }
    
    @FXML
    private void Logout(ActionEvent event) throws IOException{
        App.setRoot("login", null, null, null, null, null);
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
    private void toAttendance(ActionEvent event) throws IOException {
        App.setRoot("attendance", userName.getText(), employeeNumber.getText(), null, null,  currentMonth);
    }

    @FXML
    private void toPayroll(ActionEvent event) throws IOException {
        App.setRoot("payroll", userName.getText(), employeeNumber.getText(), null, null, currentMonth);
    }
    
}
