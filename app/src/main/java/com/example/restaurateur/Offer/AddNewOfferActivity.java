package com.example.restaurateur.Offer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.restaurateur.R;

public class AddNewOfferActivity extends AppCompatActivity {

    private TextInputLayout textInputFoodName, textInputFoodPrice, textInputFoodQuantity, textInputFoodDescription;
    private EditText etFoodName, etFoodPrice, etFoodQuantity, etFoodDescription;
    private Button btnCancel, btnSave;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_offer_item);
        Intent receivedIntent = getIntent();
        category =  receivedIntent.getExtras().getString("category");

        String title = getString(R.string.title_new_food);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        textInputFoodName = findViewById(R.id.text_input_food_name);
        textInputFoodPrice = findViewById(R.id.text_input_food_price);
        textInputFoodQuantity = findViewById(R.id.text_input_food_quantity);
        textInputFoodDescription = findViewById(R.id.text_input_food_description);
        etFoodName = findViewById(R.id.edit_text_input_food_name);
        etFoodName.setHorizontallyScrolling(false);
        etFoodName.setLines(2);
        etFoodPrice = findViewById(R.id.edit_text_input_food_price);
        etFoodQuantity = findViewById(R.id.edit_text_input_food_quantity);
        etFoodDescription = findViewById(R.id.edit_text_input_food_description);
        etFoodDescription.setHorizontallyScrolling(false);
        etFoodDescription.setLines(3);
        btnCancel = findViewById(R.id.etOfferBtnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave = findViewById(R.id.etOfferBtnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFoodInput()) {
                    Intent retIntent = new Intent(getApplicationContext(), OffersMainFragment.class);
                    Bundle bn = new Bundle();
                    bn.putString("category", category);
                    bn.putString("foodName", etFoodName.getText().toString());
                    bn.putString("foodPrice", etFoodPrice.getText().toString());
                    bn.putString("foodQuantity", etFoodQuantity.getText().toString());
                    bn.putString("foodDescription", etFoodDescription.getText().toString());
                    retIntent.putExtras(bn);
                    setResult(RESULT_OK, retIntent);
                    finish();
                }
            }
        });
    }

    private boolean validateFoodInput() {
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
            textInputFoodDescription.setError(null);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
