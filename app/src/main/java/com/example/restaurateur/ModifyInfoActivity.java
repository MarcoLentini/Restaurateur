package com.example.restaurateur;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ModifyInfoActivity extends AppCompatActivity {

    private TextView tvInfoMessage;
    private EditText etEditInfo;
    private Button btnOk;
    private Button btnCancel;
    private String fieldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);

        tvInfoMessage = findViewById(R.id.textViewTypeInfo);
        etEditInfo = findViewById(R.id.editTextChangeInfo);
        btnOk = findViewById(R.id.buttonOk);
        btnCancel = findViewById(R.id.buttonCancel);

        Intent receivedIntent = getIntent();
        fieldName = receivedIntent.getExtras().getString("field");
        String fieldValue = receivedIntent.getExtras().getString("value");
        switch (fieldName) {
            case "user_name":
                tvInfoMessage.setText("Type your username");
                etEditInfo.setText(fieldValue);
                etEditInfo.setHint("username");
                etEditInfo.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                etEditInfo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                //etEditInfo.setSelectAllOnFocus(true);
                etEditInfo.selectAll();
            break;
            case "user_email":
                tvInfoMessage.setText("Type your email address");
                etEditInfo.setText(fieldValue);
                etEditInfo.setHint("example@domain.com");
                etEditInfo.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            break;
            case "user_phone_number":
                tvInfoMessage.setText("Type your phone number");
                etEditInfo.setText(fieldValue);
                etEditInfo.setInputType(InputType.TYPE_CLASS_PHONE);
            break;
            case "user_description":
                tvInfoMessage.setText("Type a short description about you");
                etEditInfo.setText(fieldValue);
                etEditInfo.setInputType(InputType.TYPE_CLASS_TEXT);
                etEditInfo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                etEditInfo.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
                etEditInfo.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etEditInfo.setMinLines(3);
            break;
            case "user_address":
                tvInfoMessage.setText("Type your address");
                etEditInfo.setText(fieldValue);
                etEditInfo.setInputType(InputType.TYPE_CLASS_TEXT);
                etEditInfo.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
                etEditInfo.selectAll();
                break;
            case "user_notification":
                tvInfoMessage.setText("Type your notification");
                etEditInfo.setText(fieldValue);
                etEditInfo.setInputType(InputType.TYPE_CLASS_TEXT);
                etEditInfo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                etEditInfo.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etEditInfo.selectAll();
                break;
            case "opening_hours":
                tvInfoMessage.setText("Type opening hours ");
                etEditInfo.setText(fieldValue);
                etEditInfo.setInputType(InputType.TYPE_CLASS_TEXT);
                etEditInfo.setInputType(InputType.TYPE_CLASS_DATETIME);
                etEditInfo.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
                etEditInfo.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etEditInfo.selectAll();
                break;
            case "delivery_service":
                tvInfoMessage.setText("Type delivery service");
                etEditInfo.setText(fieldValue);
                etEditInfo.setInputType(InputType.TYPE_CLASS_TEXT);
                etEditInfo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                etEditInfo.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etEditInfo.selectAll();
                break;
        }
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etEditInfo , InputMethodManager.SHOW_IMPLICIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent retIntent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle bn = new Bundle();
                bn.putString("field", fieldName);
                bn.putString("value", etEditInfo.getText().toString());
                retIntent.putExtras(bn);
                setResult(1, retIntent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
