package com.example.restaurateur.Reservation;

import java.io.Serializable;

public class ReservatedDish implements Serializable {

    private String dishName;
    private Double dishPrice;
    private Long dishQty;
    private String dishCategoryID;

    public ReservatedDish(String dishName, Double dishPrice, Long dishQty, String dishCategoryID) {

        this.dishName = dishName;
        this.dishPrice = dishPrice;
        this.dishQty = dishQty;
        this.dishCategoryID = dishCategoryID;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Double getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(Double dishPrice) {
        this.dishPrice = dishPrice;
    }

    public Long getDishQty() {
        return dishQty;
    }

    public void setDishQty(Long dishQty) {
        this.dishQty = dishQty;
    }

    public String getDishCategoryID() {
        return dishCategoryID;
    }

    public void setDishCategoryID(String dishCategoryID) {
        this.dishCategoryID = dishCategoryID;
    }
}
