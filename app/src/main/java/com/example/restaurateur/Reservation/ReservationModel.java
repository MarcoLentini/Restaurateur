package com.example.restaurateur.Reservation;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;

public class ReservationModel implements Comparable<ReservationModel>, Serializable {

    private String reservation_id;
    private Long rs_id;
    private String cust_id;
    private Timestamp timestamp;
    private String notes;
    private String cust_phone;
    private ArrayList<ReservatedDish> dishesArrayList;
    private String rs_status;
    private Double total_income;
    private String rest_address;

    public ReservationModel(String reservation_id, Long rs_id, String cust_id, Timestamp timestamp, String notes,
                            String cust_phone, ArrayList<ReservatedDish> dishesArrayList,
                            String rs_status, Double total_income, String rest_address) {
        this.reservation_id = reservation_id;
        this.rs_id = rs_id;
        this.cust_id = cust_id;
        this.timestamp = timestamp;
        this.notes = notes;
        this.cust_phone = cust_phone;
        this.dishesArrayList = dishesArrayList;
        this.rs_status = rs_status;
        this.total_income = total_income;
        this.rest_address = rest_address;
    }

    public Long getRs_id() {
        return rs_id;
    }

    public void setRs_id(Long rs_id) {
        this.rs_id = rs_id;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCust_phone() {
        return cust_phone;
    }

    public void setCust_phone(String cust_phone) {
        this.cust_phone = cust_phone;
    }

    public ArrayList<ReservatedDish> getDishesArrayList() {
        return dishesArrayList;
    }

    public void setDishesArrayList(ArrayList<ReservatedDish> dishesArrayList) {
        this.dishesArrayList = dishesArrayList;
    }

    public String getRs_status() {
        return rs_status;
    }

    public void setRs_status(String rs_status) {
        this.rs_status = rs_status;
    }

    public Double getTotal_income() {
        return total_income;
    }

    public void setTotal_income(Double total_income) {
        this.total_income = total_income;
    }

    /*public ReservationModel(int id, int customerId, int remainingMinutes, String notes,
                            String customerPhoneNumber, ArrayList<ReservatedDish> reservatedDishes, String state, double totalIncome) {
        this.id = id;
        this.customerId = customerId;
        this.remainingMinutes = remainingMinutes;
        this.notes = notes;
        this.customerPhoneNumber = customerPhoneNumber;
        this.reservatedDishes = reservatedDishes;
        this.state = state;
        this.totalIncome = totalIncome;
    }*/


    @Override
    public int compareTo(ReservationModel other) {
        return this.timestamp.compareTo(other.getTimestamp());
    }

    public String getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(String reservation_id) {
        this.reservation_id = reservation_id;
    }

    public String getRest_address() {
        return rest_address;
    }

    public void setRest_address(String rest_address) {
        this.rest_address = rest_address;
    }
}
