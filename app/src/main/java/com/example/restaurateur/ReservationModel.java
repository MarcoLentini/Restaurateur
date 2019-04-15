package com.example.restaurateur;

import java.sql.Timestamp;
import java.util.ArrayList;

public class ReservationModel {


    String id;
    String customer_name;
    String customer_phone_number;
    String customer_address;
    int number_of_product;
    String remarks;
    ArrayList<OrderQuantity> list_of_products;
    double total_income;
    Timestamp time_arrive;
    Timestamp time_delivery;

    public ReservationModel(String id, String customer_name, String customer_phone_number, String customer_address, int number_of_product, String remarks, ArrayList<OrderQuantity> list_of_products, double total_income, Timestamp time_arrive, Timestamp time_delivery) {
        this.id = id;
        this.customer_name = customer_name;
        this.customer_phone_number = customer_phone_number;
        this.customer_address = customer_address;
        this.number_of_product = number_of_product;
        this.remarks = remarks;
        this.list_of_products = list_of_products;

        this.total_income = total_income;
        this.time_arrive = time_arrive;
        this.time_delivery = time_delivery;
    }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }

        public String getCustomer_phone_number() {
            return customer_phone_number;
        }

        public void setCustomer_phone_number(String customer_phone_number) {
            this.customer_phone_number = customer_phone_number;
        }

        public String getCustomer_address() {
            return customer_address;
        }

        public void setCustomer_address(String customer_address) {
            this.customer_address = customer_address;
        }

        public int getNumber_of_product() {
            return number_of_product;
        }

        public void setNumber_of_product(int number_of_product) {
            this.number_of_product = number_of_product;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public ArrayList<OrderQuantity> getList_of_products() {
            return list_of_products;
        }

        public void setList_of_products(ArrayList<OrderQuantity> list_of_products) {
            this.list_of_products = list_of_products;
        }

        public double getTotal_income() {
            return total_income;
        }

        public void setTotal_income(float total_income) {
            this.total_income = total_income;
        }
    public Timestamp getTime_arrive() {
        return time_arrive;
    }

    public void setTime_arrive(Timestamp time_arrive) {
        this.time_arrive = time_arrive;
    }

    public Timestamp getTime_delivery() {
        return time_delivery;
    }

    public void setTime_delivery(Timestamp time_delivery) {
        this.time_delivery = time_delivery;
    }

    public static class OrderQuantity {
        String food_name;
        int quantity;
        double price;

        public OrderQuantity(String food_name, int quantity, double price) {
            this.food_name = food_name;
            this.quantity = quantity;
            this.price = price;
        }



        public String getFood_name() {
            return food_name;
        }

        public void setFood_name(String food_name) {
            this.food_name = food_name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }
    }
}
