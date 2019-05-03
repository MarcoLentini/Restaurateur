package com.example.restaurateur.Offer;

public class Category {

    private String category;

    public Category(String category) {
        this.category = category;
    }

    public Category(String menu_name, String image_url, String state) {
    }

    public String getCategoryName() {
        return category;
    }

    public void setCategoryName(String category) {
        this.category = category;
    }

}
