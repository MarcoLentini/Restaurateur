package com.example.restaurateur.Offer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.restaurateur.R;

public class EditOfferActivity extends AppCompatActivity {

    private TextInputLayout textInputFoodName, textInputFoodPrice, textInputFoodQuantity, textInputFoodDescription;
    private EditText etFoodName, etFoodPrice, etFoodQuantity, etFoodDescription;
    private Button btnCancel, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dishes);

        Intent receivedIntent = getIntent();
        String foodName = receivedIntent.getExtras().getString("foodName");
        String foodDescription = receivedIntent.getExtras().getString("foodDescription");
        int foodId = receivedIntent.getExtras().getInt("foodId");
        int foodImage = receivedIntent.getExtras().getInt("foodImage");
        Integer foodQuantity = receivedIntent.getExtras().getInt("foodQuantity");
        Double foodPrice = receivedIntent.getExtras().getDouble("foodPrice");
        Boolean foodState = receivedIntent.getExtras().getBoolean("foodState");

        String title = getString(R.string.show_dish);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        textInputFoodName = findViewById(R.id.text_input_food_name_e);
        textInputFoodPrice = findViewById(R.id.text_input_food_price);
        textInputFoodQuantity = findViewById(R.id.text_input_food_quantity);
        textInputFoodDescription = findViewById(R.id.text_input_food_description);

        etFoodName = findViewById(R.id.edit_text_input_food_name_e);
        etFoodName.setHorizontallyScrolling(false);
        etFoodName.setLines(2);
        etFoodName.setText(foodName);
        etFoodPrice = findViewById(R.id.edit_text_input_food_price_e);
        etFoodPrice.setText(foodPrice.toString());
        etFoodQuantity = findViewById(R.id.edit_text_input_food_quantity_e);
        etFoodQuantity.setText(foodQuantity.toString());
        etFoodDescription = findViewById(R.id.edit_text_input_food_description_e);
        etFoodDescription.setHorizontallyScrolling(false);
        etFoodDescription.setLines(3);
        etFoodDescription.setText(foodDescription);
        btnCancel = findViewById(R.id.etOfferBtnCancel_e);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave = findViewById(R.id.etOfferBtnSave_e);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFoodName()) {
                    Intent retIntent = new Intent(getApplicationContext(), OffersDishFragment.class);
                    Bundle bn = new Bundle();
                    bn.putInt("foodId", foodId);
                    bn.putString("foodName", etFoodName.getText().toString());
                    bn.putDouble("foodPrice", Double.parseDouble(etFoodPrice.getText().toString()));
                    bn.putInt("foodQuantity", Integer.parseInt(etFoodQuantity.getText().toString()));
                    bn.putString("foodDescription", etFoodDescription.getText().toString());
                    bn.putInt("foodImage", foodImage);
                    bn.putBoolean("foodState", foodState);
                    retIntent.putExtras(bn);
                    setResult(RESULT_OK, retIntent);
                    finish();
                }
            }
        });
    }

    private boolean validateFoodName() {
        String foodNameInput = etFoodName.getText().toString();
        String foodPriceInput = etFoodPrice.getText().toString();
        String foodQuantityInput = etFoodQuantity.getText().toString();
        String foodDescriptionInput = etFoodDescription.getText().toString();
        if(foodNameInput.isEmpty()){
            textInputFoodName.setError("Field can't be empty");
            return false;
        } else
            textInputFoodName.setError(null);
        if(foodPriceInput.isEmpty()) {
            textInputFoodPrice.setError("Field can't be empty");
            return false;
        } else
            textInputFoodPrice.setError(null);
        if(foodQuantityInput.isEmpty()) {
            textInputFoodQuantity.setError("Field can't be empty");
            return false;
        } else
            textInputFoodQuantity.setError(null);
        if(foodDescriptionInput.isEmpty()) {
            textInputFoodDescription.setError("Field can't be empty");
            return false;
        } else
            textInputFoodQuantity.setError(null);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
