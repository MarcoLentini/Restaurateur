package com.example.restaurateur;

import com.example.restaurateur.Reservation.ReservationState;

public class MyReservationsData {

    static Integer[] id = {150, 151, 152, 153, 154, 155, 156, 157, 158, 519, 110,
            150, 151, 152, 153, 154, 155, 156, 157, 158, 519, 110};

    static Integer[][] orderedDish = { {21, 10, 15, 12, 5, 4, 2, 1}, {4}, {3, 1}, {4, 7, 8},
                                        {9, 11}, {1}, {4,8}, {9, 7}, {9, 7}, {9, 7}, {2, 3},
            {21, 10, 22, 4, 5, 4, 2, 1}, {4}, {3, 1}, {4, 7, 8},
            {9, 11}, {1}, {4,8}, {9, 7}, {9, 7}, {9, 7}, {9, 7}};
    static Integer[][] multiplierDish = { {2, 1, 3, 4, 5, 4, 2, 1}, {9}, {2, 1}, {2, 2, 2},
                                            {2, 1}, {1}, {3,5}, {1, 1}, {9, 7}, {9, 7}, {9, 7},
                                        {2, 1, 3, 4, 5, 4, 2, 1}, {9}, {2, 1}, {2, 2, 2},
                                        {2, 1}, {1}, {3,5}, {1, 1}, {9, 7}, {9, 7}, {9, 7}};

    static Integer[] customerId = {1501123, 2131211, 2181208, 2101111, 2131219, 1131211, 3131211,
            2131289, 1312951, 131234, 2136257, 1501123, 2131211, 2181208, 2101111, 2131219, 1131211, 3131211,
            2131289, 1312951, 131234, 2136257};

    static Double[] totalIncome = {12.50, 7.20, 15.0, 15.30, 10.0, 1.50, 0.90, 15.70, 158.0, 200.0, 11.0,
            23.0, 25.0, 11.89, 9.45, 4.50, 7.65, 15.6, 15.7, 15.8, 125.0, 10.0};

    static Integer[] remainingMinutes = {25, 42, 15, 120, 17, 12, 9, 32, 12, 18, 119, 19,
                                    34, 45, 61, 76, 82, 3, 1, 0, 12, 110};

    static String[] notes = {"", "Poco sale", "Gradierei tanto tantissimo formaggio sulla pasta rossa al pomodoro piccante",
                        "", "", "Solo ketchup, niente maionese", "I croissant li voglio alla nutella", "", "", "In fretta!", "",
            "", "Poco sale", "Gradierei tanto tantissimo formaggio sulla pasta rossa al pomodoro piccante",
            "", "", "Solo ketchup, niente maionese", "I croissant li voglio alla nutella", "", "", "In fretta!", ""};

    static String[] customerPhoneNumber = {"3201758534", "3201758572", "3292868534", "3171796534",
            "3241758534", "3251758534", "3291758534", "3211758534", "3200758534", "3201750034", "3202908534",
            "3201758534", "3201758572", "3292868534", "3171796534",
            "3241758534", "3251758534", "3291758534", "3211758534", "3200758534", "3201750034", "3202908534"};

    static int[] reservationState = {ReservationState.STATE_PENDING, ReservationState.STATE_IN_PROGRESS,
            ReservationState.STATE_PENDING, ReservationState.STATE_IN_PROGRESS,
            ReservationState.STATE_FINISHED, ReservationState.STATE_FINISHED,
            ReservationState.STATE_PENDING, ReservationState.STATE_FINISHED,
            ReservationState.STATE_PENDING, ReservationState.STATE_IN_PROGRESS, ReservationState.STATE_IN_PROGRESS,
            ReservationState.STATE_PENDING, ReservationState.STATE_IN_PROGRESS,
            ReservationState.STATE_PENDING, ReservationState.STATE_IN_PROGRESS,
            ReservationState.STATE_FINISHED, ReservationState.STATE_FINISHED,
            ReservationState.STATE_PENDING, ReservationState.STATE_FINISHED,
            ReservationState.STATE_PENDING, ReservationState.STATE_IN_PROGRESS, ReservationState.STATE_IN_PROGRESS};
}
