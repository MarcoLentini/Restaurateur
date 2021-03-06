package com.example.restaurateur.Information;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
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

        String title;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent receivedIntent = getIntent();
        fieldName = receivedIntent.getExtras().getString("field");
        final String fieldValue = receivedIntent.getExtras().getString("value");
        switch (fieldName) {
            case "user_name":
                title=getString(R.string.username_title);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_username);
                etEditInfo.setText(fieldValue);
                etEditInfo.setHint("username");
                etEditInfo.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                etEditInfo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                //etEditInfo.setSelectAllOnFocus(true);
                etEditInfo.selectAll();
                break;
            case "user_email":
                title=getString(R.string.email_title);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_email);
                etEditInfo.setText(fieldValue);
                etEditInfo.setHint("example@domain.com");
                etEditInfo.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case "user_phone_number":
                title=getString(R.string.phone_number_title);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_phone_number);
                etEditInfo.setText(fieldValue);
                etEditInfo.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case "user_description":
                title=getString(R.string.description_title);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_description);
                etEditInfo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etEditInfo.setText(fieldValue);
                etEditInfo.selectAll();
                break;
            case "user_address":
                title=getString(R.string.address_title);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_address);
                etEditInfo.setText(fieldValue);
                etEditInfo.setInputType(InputType.TYPE_CLASS_TEXT);
                etEditInfo.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
                etEditInfo.selectAll();
                break;
            case "user_notification":
                title=getString(R.string.notification_title);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_notification);
                etEditInfo.setText(fieldValue);
                etEditInfo.setInputType(InputType.TYPE_CLASS_TEXT);
                etEditInfo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                etEditInfo.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etEditInfo.selectAll();
                break;
            case "opening_hours":
                title=getString(R.string.opening_hours_title);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_opening_hours);
                etEditInfo.setText(fieldValue);
                etEditInfo.setInputType(InputType.TYPE_CLASS_TEXT);
                etEditInfo.setInputType(InputType.TYPE_CLASS_DATETIME);
                etEditInfo.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
                etEditInfo.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etEditInfo.selectAll();
                break;
            case "delivery_service":
                title=getString(R.string.delivery_info_title);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_delivery_info);
                etEditInfo.setText(fieldValue);
                etEditInfo.setInputType(InputType.TYPE_CLASS_TEXT);
                etEditInfo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                etEditInfo.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etEditInfo.selectAll();
                break;
            case "user_password":
                title=getString(R.string.password_title);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_old_password);
                etEditInfo.setText("");
                // https://stackoverflow.com/questions/9892617/programmatically-change-input-type-of-the-edittext-from-password-to-normal-vic
                etEditInfo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;

        }
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
                if(fieldName.equals("user_password")) {
                    Log.d("password", "onClick:old "+fieldValue + " new= "+etEditInfo.getText().toString());
                    if(fieldValue.equals(etEditInfo.getText().toString())){
                        retIntent = new Intent(getApplicationContext(), ChangePwdActivity.class);
                        bn = new Bundle();

                        bn.putString("field", fieldName);

                        retIntent.putExtras(bn);
                        startActivityForResult(retIntent, 1);
                    }else{

                        Toast mioToast = Toast.makeText(ModifyInfoActivity.this,
                                getString(R.string.invalid_password),
                                Toast.LENGTH_LONG);
                        mioToast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 64);
                        InputMethodManager imm = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etEditInfo, InputMethodManager.SHOW_IMPLICIT);
                        mioToast.show();

                        etEditInfo.selectAll();


                    }
                }else if(fieldName.equals("user_address")||fieldName.equals("user_notification")||fieldName.equals("opening_hours")||fieldName.equals("delivery_service")){
                    retIntent = new Intent(getApplicationContext(), ViewMoreInfoActivity.class);
                    bn = new Bundle();
                    bn.putString("field", fieldName);
                    bn.putString("value", etEditInfo.getText().toString());
                    retIntent.putExtras(bn);
                    setResult(1, retIntent);
                    finish();
                }else {
                    retIntent = new Intent(getApplicationContext(), UserInformationActivity.class);
                    bn = new Bundle();
                    bn.putString("field", fieldName);
                    bn.putString("value", etEditInfo.getText().toString());
                    retIntent.putExtras(bn);
                    setResult(RESULT_OK, retIntent);
                    finish();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bn = new Bundle();
        if(resultCode == RESULT_OK) {
            fieldName = data.getExtras().getString("field");
            String fieldValue = data.getExtras().getString("value");

            bn.putString("field", fieldName);
            bn.putString("value", fieldValue);

            Intent retIntent = new Intent(getApplicationContext(), UserInformationActivity.class);
            retIntent.putExtras(bn);
            setResult(RESULT_OK, retIntent);
            finish();
        }else if (resultCode == 1){
            fieldName = data.getExtras().getString("field");
            String fieldValue = data.getExtras().getString("value");

            bn.putString("field", fieldName);
            bn.putString("value", fieldValue);

            Intent retIntent = new Intent(getApplicationContext(), ViewMoreInfoActivity.class);
            retIntent.putExtras(bn);
            setResult(1, retIntent);
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}