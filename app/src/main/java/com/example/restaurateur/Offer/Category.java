package com.example.restaurateur.Offer;

import com.example.restaurateur.Reservation.ReservationModel;

import java.util.ArrayList;

public class Category implements Comparable<Category>{

    private String category;
    private String categoryID;
    private Long categoryPosition;
    private ArrayList<OfferModel> dishes;

    public Category(String category, String categoryID, Long categoryPosition) {
        this.category = category;
        this.categoryID = categoryID;
        this.categoryPosition = categoryPosition;
        this.dishes = new ArrayList<>();

    }

    public Category(String category, String id) {
        this.category = category;
        this.categoryID = id;
        this.dishes = new ArrayList<>();
    }

    public String getCategoryName() {
        return category;
    }

    public void setCategoryName(String category) {
        this.category = category;
    }


    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public ArrayList<OfferModel> getDishes() {
        return dishes;
    }

    public void setDishes(ArrayList<OfferModel> dishes) {
        this.dishes = dishes;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getCategoryPosition() {
        return categoryPosition;
    }

    public void setCategoryPosition(Long categoryPosition) {
        this.categoryPosition = categoryPosition;
    }


    @Override
    public int compareTo(Category other) {
        return this.categoryPosition.compareTo(other.getCategoryPosition());
    }
}
