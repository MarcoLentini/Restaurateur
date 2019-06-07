package com.example.restaurateur.Statitics;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class RestaurantStatistics implements Parcelable {

    private String restaurantStatisticsID;
    private String reservationID;
    private String restaurantID;
    private String categoryID;
    private String dishName;
    private String hash;
    private Timestamp timestamp;
    private Long qty;
    private Double price;


    public RestaurantStatistics(String restaurantStatisticsID, String reservationID, String restaurantID, String categoryID, String dishName, String hash, Timestamp timestamp, Long qty, Double price) {
        this.restaurantStatisticsID = restaurantStatisticsID;
        this.reservationID = reservationID;
        this.restaurantID = restaurantID;
        this.categoryID = categoryID;
        this.dishName = dishName;
        this.hash = hash;
        this.timestamp = timestamp;
        this.qty = qty;
        this.price = price;
    }

    protected RestaurantStatistics(Parcel in) {
        restaurantStatisticsID = in.readString();
        reservationID = in.readString();
        restaurantID = in.readString();
        categoryID = in.readString();
        dishName = in.readString();
        hash = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
        if (in.readByte() == 0) {
            qty = null;
        } else {
            qty = in.readLong();
        }
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
    }

    public static final Creator<RestaurantStatistics> CREATOR = new Creator<RestaurantStatistics>() {
        @Override
        public RestaurantStatistics createFromParcel(Parcel in) {
            return new RestaurantStatistics(in);
        }

        @Override
        public RestaurantStatistics[] newArray(int size) {
            return new RestaurantStatistics[size];
        }
    };

    public String getRestaurantStatisticsID() {
        return restaurantStatisticsID;
    }

    public void setRestaurantStatisticsID(String restaurantStatisticsID) {
        this.restaurantStatisticsID = restaurantStatisticsID;
    }

    public String getReservationID() {
        return reservationID;
    }

    public void setReservationID(String reservationID) {
        this.reservationID = reservationID;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(restaurantStatisticsID);
        dest.writeString(reservationID);
        dest.writeString(restaurantID);
        dest.writeString(categoryID);
        dest.writeString(dishName);
        dest.writeString(hash);
        dest.writeParcelable(timestamp, flags);
        if (qty == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(qty);
        }
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
    }
}
