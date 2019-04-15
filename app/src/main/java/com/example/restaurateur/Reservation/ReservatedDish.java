package com.example.restaurateur.Reservation;

public class ReservatedDish {

    private int dishId;
    private int dishMultiplier;

    public ReservatedDish(int dishId, int dishMultiplier) {
        this.dishId = dishId;
        this.dishMultiplier = dishMultiplier;
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
