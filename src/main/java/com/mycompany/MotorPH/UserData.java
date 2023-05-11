/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.MotorPH;

/**
 *
 * @author Rolis
 */
public class UserData {
    private String user;
    private String pass;
    private String netPay;
    private String numOfHours;

    public String getUser() {
        return user;
    }
    
    public String getPass() {
        return pass;
    }
    
    public String getNetPay() {
        return netPay;
    }
    
    public String getNumOfHours() {
        return numOfHours;
    }

    public void setData(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }
    
    public void setNetPay(String netPay){
        this.netPay = netPay;
    }
    
    public void setNumOfHours(String numOfHours){
        this.numOfHours = numOfHours;
    }

    void setDataModel(String user) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
