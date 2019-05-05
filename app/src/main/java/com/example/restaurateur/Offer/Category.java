package com.example.restaurateur.Offer;

public class Category {

    private String category;
    private String categoryID;

    public Category(String category, String id) {
        this.category = category;
        this.categoryID = id;
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
}
