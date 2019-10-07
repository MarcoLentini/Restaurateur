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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurateur.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifyInfoActivity extends AppCompatActivity {
    private ProgressBar progressBarSavingChanges;

    private TextView tvInfoMessage;
    private EditText etEditInfo;
    private Button btnOk;
    private Button btnCancel;
    private String fieldName;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private static final String restaurantDataFile = "RestaurantDataFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        progressBarSavingChanges = findViewById(R.id.progressBarSavingChanges);

        tvInfoMessage = findViewById(R.id.textViewTypeInfo);
        etEditInfo = findViewById(R.id.editTextChangeInfo);
        btnOk = findViewById(R.id.buttonOk);
        btnCancel = findViewById(R.id.buttonCancel);
        String title;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        auth=FirebaseAuth.getInstance();
        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            finish();
        }

        //Get Firestore instance
        db = FirebaseFirestore.getInstance();

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
                tvInfoMessage.setText(R.string.insert_old_password);
                etEditInfo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case "new_mail":
                title=getString(R.string.email_title);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_email);

                etEditInfo.setText(fieldValue);
                etEditInfo.setHint("example@domain.com");
                etEditInfo.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                etEditInfo.selectAll();
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

        etEditInfo.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                btnOk.performClick();
                InputMethodManager imm1 = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.showSoftInput(etEditInfo, InputMethodManager.SHOW_IMPLICIT);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }

            return false;
        });
        btnOk.setOnClickListener(v -> {
            progressBarSavingChanges.setVisibility(View.VISIBLE);

            Intent intent;
            Bundle Bn;
            String userName;
            String userEmail;
            String userPhoneNumber;

            switch (fieldName) {
                case "user_password":

                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), etEditInfo.getText().toString());
                    // Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnSuccessListener(aut -> {
                                progressBarSavingChanges.setVisibility(View.GONE);

                                Intent retIntent;
                                Bundle bn;
                                Log.d("modifyInfo", "User re-authenticated.");
                                retIntent = new Intent(getApplicationContext(), ChangePwdActivity.class);
                                startActivity(retIntent);
                                finish();
                            })
                            .addOnFailureListener(noaut -> {
                                progressBarSavingChanges.setVisibility(View.GONE);

                                Log.d("modifyInfo", "User failed re-authenticated.");
                                Toast mioToast = Toast.makeText(ModifyInfoActivity.this,
                                        getString(R.string.invalid_password),
                                        Toast.LENGTH_LONG);
                                mioToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 64);
                                InputMethodManager imm12 = (InputMethodManager)
                                        getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm12.showSoftInput(etEditInfo, InputMethodManager.SHOW_IMPLICIT);
                                mioToast.show();

                                etEditInfo.selectAll();
                            });


                    break;
                case "user_name":
                    userName = etEditInfo.getText().toString();
                    if (!isValidUsername(userName)) {
                        Map<String, Object> user_name = new HashMap<>();
                        user_name.put("username", userName);
                        db.collection("users").document(auth.getCurrentUser().getUid()).update(user_name)
                                .addOnSuccessListener(task -> {
                                    progressBarSavingChanges.setVisibility(View.GONE);

                                    Intent retIntent;
                                    Bundle bn;
                                    Toast.makeText(ModifyInfoActivity.this, getString(R.string.username_updated), Toast.LENGTH_LONG).show();
                                    retIntent = new Intent(getApplicationContext(), UserInformationActivity.class);
                                    bn = new Bundle();
                                    bn.putString("field", fieldName);
                                    bn.putString("value", etEditInfo.getText().toString());
                                    retIntent.putExtras(bn);
                                    setResult(RESULT_OK, retIntent);
                                    finish();
                                })
                                .addOnFailureListener((task -> {
                                    progressBarSavingChanges.setVisibility(View.GONE);

                                    Log.d("ModifyInfo", "failed update username");

                                    Toast.makeText(ModifyInfoActivity.this, getString(R.string.username_failed_update), Toast.LENGTH_LONG).show();
                                }));
                    } else {
                        Toast mioToast = Toast.makeText(ModifyInfoActivity.this,
                                getString(R.string.invalid_username),
                                Toast.LENGTH_LONG);
                        mioToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 64);
                        InputMethodManager imm12 = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm12.showSoftInput(etEditInfo, InputMethodManager.SHOW_IMPLICIT);
                        mioToast.show();
                        progressBarSavingChanges.setVisibility(View.GONE);

                        etEditInfo.selectAll();
                    }
                    break;
                case "user_email":
                     credential = EmailAuthProvider
                            .getCredential(user.getEmail(), etEditInfo.getText().toString());
                    // Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnSuccessListener(aut -> {
                                progressBarSavingChanges.setVisibility(View.GONE);

                                Log.d("modifyInfo", "User re-authenticated.");
                                Intent intent1 = new Intent(getApplicationContext(), ModifyInfoActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("field", "new_mail");
                                bundle.putString("value", fieldValue);
                                intent1.putExtras(bundle);
                                startActivity(intent1);
                            })
                            .addOnFailureListener(noaut -> {
                                Log.d("modifyInfo", "User failed re-authenticated.");
                                Toast mioToast = Toast.makeText(ModifyInfoActivity.this,
                                        getString(R.string.invalid_password),
                                        Toast.LENGTH_LONG);
                                mioToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 64);
                                InputMethodManager imm12 = (InputMethodManager)
                                        getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm12.showSoftInput(etEditInfo, InputMethodManager.SHOW_IMPLICIT);
                                mioToast.show();
                                progressBarSavingChanges.setVisibility(View.GONE);

                                etEditInfo.selectAll();
                            });
                    break;
                case "new_mail":
                    userEmail = etEditInfo.getText().toString();
                    if (isValidEmail(userEmail)) {
                        Map<String, Object> user_email = new HashMap<>();
                        user_email.put("email", userEmail);
                        user.updateEmail(userEmail)
                                .addOnSuccessListener(task -> {
                                    db.collection("users").document(user.getUid()).update(user_email)
                                            .addOnSuccessListener(task1 -> {
                                                progressBarSavingChanges.setVisibility(View.GONE);

                                                Toast.makeText(ModifyInfoActivity.this, getString(R.string.email_updated), Toast.LENGTH_LONG).show();
                                                signOut();
                                                finish();
                                            })
                                            .addOnFailureListener(task1 -> {
                                                progressBarSavingChanges.setVisibility(View.GONE);

                                                Log.d("BikerID", "Failed tast user db"+task1.getMessage());
                                                Toast.makeText(ModifyInfoActivity.this, getString(R.string.email_failed_updated), Toast.LENGTH_LONG).show();
                                                ;
                                                etEditInfo.selectAll();
                                            });
                                })
                                .addOnFailureListener(task2 -> {
                                    progressBarSavingChanges.setVisibility(View.GONE);

                                    Log.d("BikerID", "Failed update email auth"+task2.getMessage());
                                    Toast.makeText(ModifyInfoActivity.this, getString(R.string.email_failed_updated), Toast.LENGTH_LONG).show();
                                    etEditInfo.selectAll();
                                });
                    } else {
                        Toast mioToast = Toast.makeText(ModifyInfoActivity.this,
                                getString(R.string.invalid_mail),
                                Toast.LENGTH_LONG);
                        mioToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 64);
                        InputMethodManager imm12 = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm12.showSoftInput(etEditInfo, InputMethodManager.SHOW_IMPLICIT);
                        mioToast.show();
                        progressBarSavingChanges.setVisibility(View.GONE);

                        etEditInfo.selectAll();
                    }
                    break;
                case "user_phone_number":
                    userPhoneNumber = etEditInfo.getText().toString();
                    if (isValidPhone(userPhoneNumber)) {
                        Map<String, Object> user_phone = new HashMap<>();
                        user_phone.put("phone", userPhoneNumber);
                        db.collection("users").document(auth.getCurrentUser().getUid()).update(user_phone)
                                .addOnSuccessListener((task -> {
                                    progressBarSavingChanges.setVisibility(View.GONE);

                                    Intent retIntent;
                                    Bundle bn;
                                    Toast.makeText(ModifyInfoActivity.this, getString(R.string.phone_updated), Toast.LENGTH_LONG).show();
                                    retIntent = new Intent(getApplicationContext(), UserInformationActivity.class);
                                    bn = new Bundle();
                                    bn.putString("field", fieldName);
                                    bn.putString("value", etEditInfo.getText().toString());
                                    retIntent.putExtras(bn);
                                    setResult(RESULT_OK, retIntent);
                                    finish();
                                }))
                                .addOnFailureListener(task -> {
                                    progressBarSavingChanges.setVisibility(View.GONE);

                                    Log.d("BikerID", "Failed update phone");
                                    Toast.makeText(ModifyInfoActivity.this, getString(R.string.phone_failed_updated), Toast.LENGTH_LONG).show();
                                    etEditInfo.selectAll();
                                });

                    } else {
                        Toast mioToast = Toast.makeText(ModifyInfoActivity.this,
                                getString(R.string.invalid_phone),
                                Toast.LENGTH_LONG);
                        mioToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 64);
                        InputMethodManager imm12 = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm12.showSoftInput(etEditInfo, InputMethodManager.SHOW_IMPLICIT);
                        mioToast.show();
                        progressBarSavingChanges.setVisibility(View.GONE);

                        etEditInfo.selectAll();
                    }
                    break;

            }

            if(fieldName.equals("user_address")||fieldName.equals("user_notification")||fieldName.equals("opening_hours")||fieldName.equals("delivery_service")){
                progressBarSavingChanges.setVisibility(View.GONE);

                intent = new Intent(getApplicationContext(), RestInformationActivity.class);
                Bn = new Bundle();
                Bn.putString("field", fieldName);
                Bn.putString("value", etEditInfo.getText().toString());
                intent.putExtras(Bn);
                setResult(1, intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bn = new Bundle();
      if (resultCode == 1){
            fieldName = data.getExtras().getString("field");
            String fieldValue = data.getExtras().getString("value");

            bn.putString("field", fieldName);
            bn.putString("value", fieldValue);

            Intent retIntent = new Intent(getApplicationContext(), RestInformationActivity.class);
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
    public boolean isValidUsername(final String username) {

        if(username.length()<3)
            return true;
        else return false;
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public boolean isValidEmail(final String email) {

        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";;

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);

        return matcher.matches();

    }
    public boolean isValidPhone(final String phone) {

        Pattern pattern;
        Matcher matcher;

        final String PHONE_PATTERN = "^[+]?[0-9]{10,13}$";

        pattern = Pattern.compile(PHONE_PATTERN);
        matcher = pattern.matcher(phone);

        return matcher.matches();

    }
    //sign out method
    public void signOut() {
        auth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();

    }
}