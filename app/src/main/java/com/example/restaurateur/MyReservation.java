package com.example.restaurateur;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

public class MyReservation {
    static ArrayList<ReservationModel> reservation_list = new ArrayList<ReservationModel>((Arrays.asList(new ReservationModel("A1", "Marco", "34000000", "via prov", 2, "", new ArrayList<ReservationModel.OrderQuantity>(Arrays.asList(new ReservationModel.OrderQuantity("pizza", 1, 3.5), new ReservationModel.OrderQuantity("pizza2", 1, 4.5))), 8.0, new Timestamp(12), new Timestamp(13)))));

}
