/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.MotorPH;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mycompany.MotorPH.UtilityClasses.Constants;
import com.mycompany.MotorPH.UtilityClasses.JsonFiles;
import com.mycompany.MotorPH.UtilityClasses.NameIterator;
import com.mycompany.MotorPH.UtilityClasses.SalaryCalculator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.control.ChoiceBox;

/**
 * FXML Controller class
 *
 * @author Rolis
 */
public class Payroll implements Initializable {

    @FXML
    private Label userName;
    @FXML
    private Label currentTime;
    @FXML
    private Label currentDate;
    @FXML
    private AnchorPane userTab;
    @FXML
    private ChoiceBox<Month> attendanceChoose;
    @FXML
    private Label monthChosen;
    public TableView<AttendanceRecord> attendanceTable;
    
    private DataModel dataModel = new DataModel();
    
    private Dashboard dashboard = new Dashboard();
    
    @FXML
    private Circle userHead;
    
    private String employeeNumber;
    
    private String ENKey = "employeeNum";
    
    private Month currentMonth; /*YearMonth.now().getMonth();*/
    
    @FXML
    private Label hoursRendered;
    @FXML
    private Label grossSalary;
    @FXML
    private Label hourlyRate;
    @FXML
    private Label sssDeduction;
    @FXML
    private Label philhealthDeduction;
    @FXML
    private Label pagibigDeduction;
    @FXML
    private Label totalDeduction;
    @FXML
    private Label netSalary;
    @FXML
    private Label withHolding;
    @FXML
    private Label taxable;
    @FXML
    private Label salaryAfterTax;
    @FXML
    private Label totalAllowances;
    @FXML
    private Label riceSubsidy;
    @FXML
    private Label phoneAllowance;
    @FXML
    private Label clothingAllowance;
    @FXML
    private Label grossSalaryCopy;
    @FXML
    private Label totalAllowancesCopy;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Constants.setCurrentTime(currentTime);
        Constants.setCurrentDate(currentDate);
        attendanceChoose.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        try {
            computeGrossPay();
            setHoursRendered();
        } catch (IOException ex) {
        Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        });
    }    
    
    /*****************************
     * GENERAL METHODS 
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
        employeeNumber = employeeNum;
        
        Constants.setChoiceBoxValues(attendanceChoose, currentMonth);
        computeGrossPay();
        setMonthUpdates();
    }
    
    public void setMonthUpdates(){
    attendanceChoose.valueProperty().addListener((observable, oldValue, newValue) -> {
        currentMonth = newValue;
    });
    }
    
    /*****************************
     * SALARY COMPUTATION
     *****************************/
    
    private void computeGrossPay() throws IOException{
        JsonArray jsonAttendance = JsonFiles.getAttendanceJSON();
        JsonArray jsonEmployees = JsonFiles.getEmployeesJSON();
        int numberOfElements = SalaryCalculator.getNumberOfElements(jsonAttendance, ENKey, employeeNumber, currentMonth);
        long[] attendance = SalaryCalculator.getAttendance(jsonAttendance, ENKey, employeeNumber, numberOfElements, currentMonth);
        String sumOfAttendance = Long.toString(SalaryCalculator.getSumOfAttendance(attendance));
        
        double hourlyPay = Double.parseDouble(this.hourlyRate.getText());
        double numOfHours = Double.parseDouble(sumOfAttendance);
        double riceSubsidy = Double.parseDouble(NameIterator.nameIterator(jsonEmployees, ENKey, employeeNumber, "rice_subsidy"));
        double phoneAllowance = Double.parseDouble(NameIterator.nameIterator(jsonEmployees, ENKey, employeeNumber, "phone_allowance"));
        double clothingAllowance = Double.parseDouble(NameIterator.nameIterator(jsonEmployees, ENKey, employeeNumber, "clothing_allowance"));
        double totalAllowances = riceSubsidy + phoneAllowance + clothingAllowance;
        double grossSalary = hourlyPay * numOfHours;
        
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
        double salaryAfterTax = netSalary;
        
        if (netSalary != 0)
            netSalary = netSalary + totalAllowances;
        
        DecimalFormat df = new DecimalFormat("#.##");
        
        this.hoursRendered.setText(Double.toString(numOfHours));
        hourlyRate.setText(NameIterator.nameIterator(jsonEmployees, ENKey, employeeNumber, "hourly_rate"));
        this.grossSalary.setText(df.format(grossSalary));
        this.grossSalaryCopy.setText(df.format(grossSalary));
        this.philhealthDeduction.setText(df.format(philHealthDeduction));
        this.sssDeduction.setText(df.format(sssDeduction));
        this.pagibigDeduction.setText(df.format(pagibigDeduction));
        this.totalDeduction.setText(df.format(totalDeductions));
        this.taxable.setText(df.format(initialSalary));
        this.withHolding.setText(df.format(withHoldingTax));
        this.riceSubsidy.setText(Double.toString(riceSubsidy));
        this.phoneAllowance.setText(Double.toString(phoneAllowance));
        this.clothingAllowance.setText(Double.toString(clothingAllowance));
        this.totalAllowances.setText(Double.toString(totalAllowances));
        this.totalAllowancesCopy.setText(Double.toString(totalAllowances));
        this.salaryAfterTax.setText(df.format(salaryAfterTax));
        this.netSalary.setText("₱" + df.format(netSalary));
    }
    
    public void setHoursRendered() throws IOException{
        JsonArray json = JsonFiles.getAttendanceJSON();
        int numberOfElements = SalaryCalculator.getNumberOfElements(json, ENKey, employeeNumber, currentMonth);
        
        long[] attendance = SalaryCalculator.getAttendance(json, ENKey, employeeNumber, numberOfElements, currentMonth);
        
        long[] filteredAttendance = Arrays.stream(attendance).filter(num -> num != 0).toArray();    
        
        String sumOfAttendance = Long.toString(SalaryCalculator.getSumOfAttendance(attendance));
        
        hoursRendered.setText(sumOfAttendance);
    }

    /*****************************
     * OTHER IMPORTANT METHODS
     *****************************/
    
    
    
    
    /*****************************
     * EVENTS
     *****************************/
    
    @FXML
    private void userHead(MouseEvent event) {
        if (userTab.isVisible()) {
            userTab.setVisible(false);
            return;
        }
        userTab.setVisible(true);
    }

    @FXML
    private void Logout(ActionEvent event) throws IOException{
        App.setRoot("login", null, null, null, null,  null);
    }

    @FXML
    private void goToDashboard(ActionEvent event) throws IOException {
        App.setRoot("dashboard", userName.getText(), employeeNumber, null, null,  currentMonth);
    }

    
}
