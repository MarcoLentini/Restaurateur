package com.example.restaurateur.Offer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.restaurateur.Information.ModifyInfoActivity;
import com.example.restaurateur.R;
import com.example.restaurateur.UserInformationActivity;

public class AddNewCategory extends AppCompatActivity {

    private TextView tvInfoMessage;
    private EditText etEditInfo;
    private Button btnOk;
    private Button btnCancel;
    private String fieldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_category);

        tvInfoMessage = findViewById(R.id.textViewNewCategory);
        etEditInfo = findViewById(R.id.editTextNewCategory);
        btnOk = findViewById(R.id.buttonOkNewCategory);
        btnCancel = findViewById(R.id.buttonCancelNewCategory);

        String title=getString(R.string.new_category_title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(title);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etEditInfo , InputMethodManager.SHOW_IMPLICIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        etEditInfo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnOk.performClick();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(etEditInfo, InputMethodManager.SHOW_IMPLICIT);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                }

                return false;
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent retIntent;
                Bundle bn;
               String category= etEditInfo.getText().toString();
                if(!category.equals("")) {
                    retIntent = new Intent();
                    bn = new Bundle();
                    bn.putString("category", etEditInfo.getText().toString());
                    retIntent.putExtras(bn);
                    setResult(RESULT_OK, retIntent);
                    finish();
                }else{
                    Toast mioToast = Toast.makeText(AddNewCategory.this,
                            getString(R.string.invalid_category),
                            Toast.LENGTH_LONG);
                    mioToast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 64);
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(etEditInfo, InputMethodManager.SHOW_IMPLICIT);
                    mioToast.show();
                    etEditInfo.selectAll();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
