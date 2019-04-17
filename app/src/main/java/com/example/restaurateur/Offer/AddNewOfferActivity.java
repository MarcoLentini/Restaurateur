package com.example.restaurateur.Offer;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import com.example.restaurateur.R;

public class AddNewOfferActivity extends AppCompatActivity {

    private TextInputLayout textInputFoodName;
    private TextInputLayout textInputFoodDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_offer_item);

        textInputFoodName = findViewById(R.id.text_input_food_name);
       // textInputFoodDescription = findViewById(R.id.text_input_food_description);


    }

    private boolean validateFoodName(){
        String foodNameInput = textInputFoodName.getEditText().toString().trim();
        if(foodNameInput.isEmpty()){
            textInputFoodName.setError("File can't be empty");
            return false;
        }else{
            textInputFoodName.setError(null);
            return true;
        }
    }



}
