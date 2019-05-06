package com.example.restaurateur.Offer;

import java.util.ArrayList;

public class Category {

    private String category;
    private String categoryID;
    private ArrayList<OfferModel> dishes;

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
}
