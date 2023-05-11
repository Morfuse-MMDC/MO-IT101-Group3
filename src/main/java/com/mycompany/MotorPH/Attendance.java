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
import com.mycompany.MotorPH.UtilityClasses.SalaryCalculator;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import java.util.concurrent.atomic.AtomicInteger;
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
public class Attendance implements Initializable {

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
    @FXML
    public TableView<AttendanceData> attendanceTable;
    
    private UserData dataModel = new UserData();
    
    private Dashboard dashboard = new Dashboard();
    
    @FXML
    private Circle userHead;
    
    private String employeeNumber;
    
    private String ENKey = "employeeNum";
    
    private Month currentMonth; /*YearMonth.now().getMonth();*/
    @FXML
    private Label presents;
    @FXML
    private Label hoursRendered;
    @FXML
    private Label lates;
    @FXML
    private Label absents;
    
    private AtomicInteger presentsNum = new AtomicInteger(0);
    
    private AtomicInteger hoursRenderedNum = new AtomicInteger(0);
    
    private AtomicInteger latesNum = new AtomicInteger(0);
    
    private AtomicInteger absentsNum = new AtomicInteger(0);


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
                resetSummaryValues();
                getTableContent();
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
        setMonthUpdates();
    }
    
    public void setMonthUpdates(){
        attendanceChoose.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentMonth = newValue;
        });
    }
    
    /*****************************
     * TABLE VALUES
     *****************************/
    
    public void getTableContent() throws IOException{
        List<AttendanceData> attendanceRecords = loadAttendanceRecordsFromJsonFile(JsonFiles.getAttendanceJsonPath());
        ObservableList<AttendanceData> observableRecords = FXCollections.observableArrayList(attendanceRecords);
        attendanceTable.setItems(observableRecords);
        
        // Ensure that the TableView's columns are configured to display the correct properties of the objects in the ObservableList
        TableColumn<AttendanceData, String> dateColumn = (TableColumn<AttendanceData, String>) attendanceTable.getColumns().get(0);
        TableColumn<AttendanceData, String> timeInColumn = (TableColumn<AttendanceData, String>) attendanceTable.getColumns().get(1);
        TableColumn<AttendanceData, String> timeOutColumn = (TableColumn<AttendanceData, String>) attendanceTable.getColumns().get(2);
        TableColumn<AttendanceData, String> hoursRenderedColumn = (TableColumn<AttendanceData, String>) attendanceTable.getColumns().get(3);
        TableColumn<AttendanceData, String> present = (TableColumn<AttendanceData, String>) attendanceTable.getColumns().get(4);
        
        // Set the values of each column
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeInColumn.setCellValueFactory(new PropertyValueFactory<>("timeIn"));
        timeOutColumn.setCellValueFactory(new PropertyValueFactory<>("timeOut"));
        hoursRenderedColumn.setCellValueFactory(new PropertyValueFactory<>("hoursRendered"));
        present.setCellValueFactory(new PropertyValueFactory<>("present"));
    }

    public List<AttendanceData> loadAttendanceRecordsFromJsonFile(String filePath) throws IOException {
        List<AttendanceData> attendanceRecords = new ArrayList<>();

        // Load the JSON file as a JsonArray
        JsonArray jsonArray = JsonFiles.getAttendanceJSON(filePath);

        // Loop through each element in the array and create an AttendanceData object for each one
        for (JsonElement element : jsonArray) {
            
            JsonObject attendanceJson = element.getAsJsonObject();
            String employeeNum = attendanceJson.get("employeeNum").getAsString();
            LocalDateTime month = SalaryCalculator.getTimeInOrOut(attendanceJson, "time_in");
            
            if (employeeNum.equals(employeeNumber) && month.getMonth().equals(currentMonth)){
                
                String date = attendanceJson.get("date").getAsString();
                String timeIn = attendanceJson.get("time_in").getAsString();
                String timeOut = attendanceJson.get("time_out").getAsString();
                String hoursRendered = SalaryCalculator.getAttendance(attendanceJson, currentMonth, presentsNum, latesNum, absentsNum, hoursRenderedNum);
                String present = isPresent(hoursRendered);
                setSummaryLabels();
                
                AttendanceData attendanceRecord = new AttendanceData(date, timeIn, timeOut, hoursRendered + " hours", present);
                attendanceRecords.add(attendanceRecord);   
            }
            
        }
        return attendanceRecords;
    }
    
    /*****************************
     * OTHER IMPORTANT METHODS
     *****************************/
    
    public void setSummaryLabels(){
        lates.setText(latesNum.toString());
        presents.setText(presentsNum.toString());
        absents.setText(absentsNum.toString());
        hoursRendered.setText(hoursRenderedNum.toString());
    }
    
    public void resetSummaryValues(){
        this.absentsNum = new AtomicInteger(0);
        this.hoursRenderedNum = new AtomicInteger(0);
        this.latesNum = new AtomicInteger(0);
        this.presentsNum = new AtomicInteger(0);
    }
    
    public String isPresent(String hoursRendered){
        if(!hoursRendered.equals("0"))
            return "Present";
        return "Absent";
    }
    
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
        App.setRoot("login", null, null, null, null, null);
    }

    @FXML
    private void goToDashboard(ActionEvent event) throws IOException {
        App.setRoot("dashboard", userName.getText(), employeeNumber, null, null, currentMonth);
    }

    
}
