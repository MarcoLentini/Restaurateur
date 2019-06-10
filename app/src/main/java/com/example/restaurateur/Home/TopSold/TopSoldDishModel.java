package com.example.restaurateur.Home.TopSold;

public class TopSoldDishModel  {
      private String dishName;
      private Long monthlySoldQuantity;
      private String image;

    public TopSoldDishModel(String dishName, Long monthlySoldQuantity, String image) {
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

    public Long getMonthlySoldQuantity() {
        return monthlySoldQuantity;
    }

    public void setMonthlySoldQuantity(Long monthlySoldQuantity) {
        this.monthlySoldQuantity = monthlySoldQuantity;
    }

}