package com.example.restaurateur.Offer;

public class FoodOfferModel {
    String title;
    String content;
    int id_;
    int image;
    float price;
    int quantity;

    public FoodOfferModel(String title, String content, int id_, int image, float price, int quantity) {
        this.title = title;
        this.content = content;
        this.id_ = id_;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getImage() {
        return image;
    }

    public int getId() {
        return id_;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
