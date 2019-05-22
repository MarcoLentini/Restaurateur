package com.example.restaurateur.Information;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;

class RestInformationModel {

    private String rest_name;
    private String rest_descr;
    private String rest_address;
    private String rest_phone;
    private Double delivery_fee;
    private String user_id;
    private Uri rest_image;
    private ArrayList<String> tags;
   private  HashMap<String, Object> timetable;

    public RestInformationModel(String rest_name, String rest_descr, String rest_address, String rest_phone, Double delivery_fee, String user_id, Uri rest_image, ArrayList<String> tags, HashMap<String, Object> timetable) {
        this.rest_name = rest_name;
        this.rest_descr = rest_descr;
        this.rest_address = rest_address;
        this.rest_phone = rest_phone;
        this.delivery_fee = delivery_fee;
        this.user_id = user_id;
        this.rest_image = rest_image;
        this.tags=tags;
       this.timetable=timetable;
    }

    public String getRest_name() {
        return rest_name;
    }

    public void setRest_name(String rest_name) {
        this.rest_name = rest_name;
    }

    public String getRest_descr() {
        return rest_descr;
    }

    public void setRest_descr(String rest_descr) {
        this.rest_descr = rest_descr;
    }

    public String getRest_address() {
        return rest_address;
    }

    public void setRest_address(String rest_address) {
        this.rest_address = rest_address;
    }

    public String getRest_phone() {
        return rest_phone;
    }

    public void setRest_phone(String rest_phone) {
        this.rest_phone = rest_phone;
    }

    public Double getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(Double delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Uri getRest_image() {
        return rest_image;
    }

    public void setRest_image(Uri rest_image) {
        this.rest_image = rest_image;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public HashMap<String, Object> getTimetable() {
        return timetable;
    }

    public void setTimetable(HashMap<String, Object> timetable) {
        this.timetable = timetable;
    }
}
