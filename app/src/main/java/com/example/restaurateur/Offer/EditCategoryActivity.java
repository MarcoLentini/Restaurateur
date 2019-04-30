package com.example.restaurateur.Offer;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.restaurateur.R;

public class EditCategoryActivity extends AppCompatActivity {

    private TextInputLayout textInputFoodCategory;
    private EditText etFoodCategory;
    private Button btnCancel, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        String title = getString(R.string.edit_category_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        Intent receivedIntent = getIntent();
        String receivedCategoryName = receivedIntent.getExtras().getString("categoryName");
        textInputFoodCategory = findViewById(R.id.text_input_food_category);
        etFoodCategory = findViewById(R.id.edit_text_input_food_category);
        etFoodCategory.setHorizontallyScrolling(false);
        etFoodCategory.setLines(1);
        etFoodCategory.setText(receivedCategoryName);
        etFoodCategory.selectAll();
        btnCancel = findViewById(R.id.etOfferBtnCancel);
        btnCancel.setOnClickListener(v -> finish());
        btnSave = findViewById(R.id.etOfferBtnSave);
        btnSave.setOnClickListener(v -> {
            if(validateCategoryInput()) { // TODO verificare che il nuovo nome non sia già presente nella lista delle categorie
                Intent retIntent = new Intent(getApplicationContext(), OffersCategoryFragment.class);
                Bundle bn = new Bundle();
                bn.putInt("selectedPosition", receivedIntent.getExtras().getInt("selectedPosition"));
                String category = etFoodCategory.getText().toString();
                bn.putString("categoryName", category);
                retIntent.putExtras(bn);
                setResult(RESULT_OK, retIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean validateCategoryInput() {
        String foodCategoryInput = etFoodCategory.getText().toString();
        if(foodCategoryInput.isEmpty()){
            textInputFoodCategory.setError("Field can't be empty");
            return false;
        } else
            textInputFoodCategory.setError(null);

        return true;
    }
}
