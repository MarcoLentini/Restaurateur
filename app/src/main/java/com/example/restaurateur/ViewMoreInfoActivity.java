package com.example.restaurateur;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class ViewMoreInfoActivity extends AppCompatActivity {
    private TextView tvUserAddress;
    private TextView tvUserNotification;
    private TextView tvOpeningHours;
    private TextView tvDeliveryInfo;

    private TextView tvUserPassword;
    private String userPassword;

    private String userAddress;
    private String userNotification;
    private String openingHours;
    private String deliveryInfo;

    private SharedPreferences sharedPref;
    private static final String userFile = "UserDataFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_viewmore);
        String title;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String titile = getString(R.string.InfoTitle);
        getSupportActionBar().setTitle(titile);

        tvUserAddress = findViewById(R.id.textViewUserAddress);
        tvUserAddress.setOnClickListener(v -> {
            String idField = getString(R.string.address_field_id);
            invokeModifyInfoActivity(idField,userAddress);
        });

        tvUserNotification = findViewById(R.id.textViewNotification);
        tvUserNotification.setOnClickListener(v -> {
            String idField = getString(R.string.notification_field_id);
            invokeModifyInfoActivity(idField,userNotification);
        });

        tvOpeningHours = findViewById(R.id.textViewOpeningHours);
        tvOpeningHours.setOnClickListener(v -> {
            String idField = getString(R.string.opening_hours_field_id);
            invokeModifyInfoActivity(idField,openingHours);
        });

        tvDeliveryInfo = findViewById(R.id.textViewDeliveryService);
        tvDeliveryInfo.setOnClickListener(v -> {
            String idField = getString(R.string.delivery_service_field_id);
            invokeModifyInfoActivity(idField,deliveryInfo);
        });

        tvUserPassword = findViewById(R.id.textViewChangePassword);
        tvUserPassword.setOnClickListener(v -> {
            if(userPassword.equals(""))
                invokeChangePwdActivity(getString(R.string.pwd_field_id));
            else{
                String idField = getString(R.string.pwd_field_id);
                invokeModifyInfoActivity(idField, userPassword);

            }
        });

        sharedPref =getSharedPreferences(userFile, Context.MODE_PRIVATE);
        String userName = sharedPref.getString("userName","");

        userAddress =sharedPref.getString("userAddress","");
        if (!userAddress.equals(""))
            tvUserAddress.setText(userAddress);

        userNotification =sharedPref.getString("userNotification","");
        if (!userNotification.equals(""))
            tvUserNotification.setText(userNotification);

        openingHours =sharedPref.getString("openingHours","");
        if (!openingHours.equals(""))
            tvOpeningHours.setText(openingHours);

        deliveryInfo =sharedPref.getString("deliveryInfo","");
        if (!deliveryInfo.equals(""))
            tvDeliveryInfo.setText(deliveryInfo);

        userPassword = sharedPref.getString("userPassword", "");

    }
    private void invokeModifyInfoActivity(String fieldName, String fieldNameValue){
        Intent intent = new Intent(getApplicationContext(), ModifyInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("field", fieldName);
        bundle.putString("value", fieldNameValue);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void invokeChangePwdActivity( String fieldName) {
        Intent intent = new Intent(getApplicationContext(), ChangePwdActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("field", fieldName);

        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(resultCode == 1) {
            switch (data.getExtras().getString("field")) {

                case "user_address":
                    userAddress = data.getExtras().getString("value");
                    if(!userAddress.equals("")) {
                        editor.putString("userDescription", userAddress);
                        editor.commit();
                        tvUserAddress.setText(userAddress);
                    }
                    break;
                case "user_notification":
                    userNotification = data.getExtras().getString("value");
                    if(!userNotification.equals("")) {
                        editor.putString("userNotification", userNotification);
                        editor.commit();
                        tvUserNotification.setText(userNotification);
                    }
                    break;
                case "opening_hours":
                    openingHours = data.getExtras().getString("value");
                    if(!openingHours.equals("")) {
                        editor.putString("openingHours",openingHours );
                        editor.commit();
                        tvOpeningHours.setText(openingHours );
                    }
                    break;
                case "delivery_service":
                    deliveryInfo = data.getExtras().getString("value");
                    if(!deliveryInfo.equals("")) {
                        editor.putString("deliveryInfo",deliveryInfo );
                        editor.commit();
                        tvDeliveryInfo.setText(deliveryInfo );
                    }
                    break;
                case "user_password":
                    userPassword = data.getExtras().getString("value");
                    if(!userPassword.equals("")) {
                        editor.putString("userPassword", userPassword);
                        editor.commit();

                    }
                    break;
            }
        }
    }
}
