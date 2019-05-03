package com.example.restaurateur.Reservation;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;

public class ReservationModel implements Comparable<ReservationModel>, Serializable {

    private int id;
    private int customerId;
    private int remainingMinutes;
    private String notes;
    private String customerPhoneNumber;
    private ArrayList<ReservatedDish> reservatedDishes;
    private String state;
    private double totalIncome;

    public ReservationModel(int id, int customerId, int remainingMinutes, String notes,
                            String customerPhoneNumber, ArrayList<ReservatedDish> reservatedDishes, String state, double totalIncome) {
        this.id = id;
        this.customerId = customerId;
        this.remainingMinutes = remainingMinutes;
        this.notes = notes;
        this.customerPhoneNumber = customerPhoneNumber;
        this.reservatedDishes = reservatedDishes;
        this.state = state;
        this.totalIncome = totalIncome;
    }

    public ReservationModel(Long rs_id, String cust_id, Timestamp desidered_hour, String notes, String cust_phone, ArrayList<ReservatedDish> tmpArrayList, String rs_status, Double total_income) {
    }

    public ArrayList<ReservatedDish> getReservatedDishes() {
        return reservatedDishes;
    }

    public void setReservatedDishes(ArrayList<ReservatedDish> reservatedDishes) {
        this.reservatedDishes = reservatedDishes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getRemainingMinutes() {
        return remainingMinutes;
    }

    public void setRemainingMinutes(int remainingMinutes) {
        this.remainingMinutes = remainingMinutes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    @Override
    public int compareTo(ReservationModel other) {
        return this.getRemainingMinutes() - other.getRemainingMinutes();
    }
}
