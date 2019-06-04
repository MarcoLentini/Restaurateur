package com.example.restaurateur.Home;

import java.io.Serializable;

public class TopSoldDishModel  {
      private String dishName;
      private int monthlySoldQuantity;
      private String image;

    public TopSoldDishModel(String dishName, int monthlySoldQuantity, String image) {
        this.dishName = dishName;
        this.monthlySoldQuantity = monthlySoldQuantity;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public int getMonthlySoldQuantity() {
        return monthlySoldQuantity;
    }

    public void setMonthlySoldQuantity(int monthlySoldQuantity) {
        this.monthlySoldQuantity = monthlySoldQuantity;
    }

}
