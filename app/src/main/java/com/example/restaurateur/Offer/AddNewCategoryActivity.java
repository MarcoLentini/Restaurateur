package com.example.restaurateur.Offer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurateur.R;

public class AddNewCategoryActivity extends AppCompatActivity {

    private TextInputLayout textInputFoodCategory;
    private EditText etFoodCategory;
    private Button btnCancel, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_category);

        String title = getString(R.string.new_category_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        textInputFoodCategory = findViewById(R.id.text_input_food_category);
        etFoodCategory = findViewById(R.id.edit_text_input_food_category);
        etFoodCategory.setHorizontallyScrolling(false);
        etFoodCategory.setLines(1);
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
                    Intent retIntent = new Intent(getApplicationContext(), OffersCategoryFragment.class);
                    Bundle bn = new Bundle();
                    String category = etFoodCategory.getText().toString();
                    bn.putString("category", category);
                    retIntent.putExtras(bn);
                    setResult(RESULT_OK, retIntent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean validateFoodInput() {
        String foodCategoryInput = etFoodCategory.getText().toString();
        if(foodCategoryInput.isEmpty()){
            textInputFoodCategory.setError("Field can't be empty");
            return false;
        } else
            textInputFoodCategory.setError(null);

        return true;
    }
}
