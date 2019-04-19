package com.example.restaurateur.Offer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.restaurateur.R;

public class EditDishesActivity extends AppCompatActivity {

    private TextInputLayout textInputFoodName, textInputFoodDescription,textInputFoodCategory ;
    private EditText etFoodName, etFoodPrice, etFoodQuantity, etFoodDescription,etFoodCategory;
    private Button btnCancel, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dishes);
        Intent receivedIntent = getIntent();
        String foodCategory=  receivedIntent.getExtras().getString("foodCategory");
        String foodName=  receivedIntent.getExtras().getString("foodName");
        String foodDescription=  receivedIntent.getExtras().getString("foodDescription");
        int foodId=  receivedIntent.getExtras().getInt("foodId");
        int foodImage=  receivedIntent.getExtras().getInt("foodImage");
        Integer foodQuantity=  receivedIntent.getExtras().getInt("foodQuantity");
        Double foodPrice=  receivedIntent.getExtras().getDouble("foodPrice");
        String foodState= receivedIntent.getExtras().getString("fooodState");

        String title = getString(R.string.show_dish);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        textInputFoodName = findViewById(R.id.text_input_food_name_e);
        textInputFoodCategory = findViewById(R.id.text_input_food_category_e);

        etFoodCategory = findViewById(R.id.edit_text_input_food_category_e);
        etFoodCategory.setText(foodCategory);
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
                Intent retIntent = new Intent(getApplicationContext(), Offers_f.class);
                Bundle bn = new Bundle();
                bn.putInt("foodId",foodId);
                bn.putString("foodCategory",etFoodCategory.getText().toString());
                bn.putString("foodName", etFoodName.getText().toString());
                bn.putString("foodPrice", etFoodPrice.getText().toString());
                bn.putString("foodQuantity", etFoodQuantity.getText().toString());
                bn.putString("foodDescription", etFoodDescription.getText().toString());
                bn.putInt("foodImage",foodImage);
                bn.putString("foodState",foodState);
                retIntent.putExtras(bn);
                setResult(RESULT_OK, retIntent);
                finish();
            }
        });
    }

    private boolean validateFoodName(){
        String foodNameInput = textInputFoodName.getEditText().toString().trim();
        String foodCategoryInput=etFoodCategory.getText().toString();
        Double foodPriceInput=Double.parseDouble(etFoodPrice.getText().toString());
        int foodQuantityInput= Integer.parseInt(etFoodQuantity.getText().toString());
        String foodDescriptionInput= etFoodDescription.getText().toString();
        int flag;
        if(foodNameInput.isEmpty()){
            textInputFoodName.setError("File can't be empty");
            flag=0;
        }else{
            textInputFoodName.setError(null);
            flag=1;
        }
        if(foodCategoryInput.isEmpty()){
            textInputFoodCategory.setError("File can't be empty");
            flag=0;
        }else{
            textInputFoodName.setError(null);
            flag=1;
        }

        if(flag==1)
            return true;
        else
            return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
