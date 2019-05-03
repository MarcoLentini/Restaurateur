package com.example.restaurateur.Reservation;

import java.io.Serializable;

public class ReservatedDish implements Serializable {

    private int dishId;
    private int dishMultiplier;

    private String dishName;
    private Double dishPrice;
    private Long dishQty;

    public ReservatedDish(int dishId, int dishMultiplier) {
        this.dishId = dishId;
        this.dishMultiplier = dishMultiplier;
    }

    public ReservatedDish(String dish_name, Long dish_qty, double dish_price) {
        this.dishName = dish_name;
        this.dishQty = dish_qty;
        this.dishPrice = dish_price;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getDishMultiplier() {
        return dishMultiplier;
    }

    public void setDishMultiplier(int dishMultiplier) {
        this.dishMultiplier = dishMultiplier;
    }
}
