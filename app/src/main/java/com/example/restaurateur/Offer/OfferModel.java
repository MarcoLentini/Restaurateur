package com.example.restaurateur.Offer;

public class OfferModel {

    private String id;
    private String name;
    private String category;
    private Double price;
    private Long quantity;
    private String description;
    private String image;
    private Boolean state;

    public OfferModel(String id, String name, String category, double price, Long quantity, String image, String description, Boolean state) {
        this.id = id;
        this.name = name;
        this.category=category;
        this.price = price;
        this.quantity=quantity;
        this.image = image;
        this.description=description;
        this.state = state;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public Long getQuantity() {
        return quantity;
    }
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
