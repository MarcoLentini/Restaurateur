package com.example.restaurateur.Information;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.Layout;
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

import com.example.restaurateur.ChooseBiker.GeocodingLocation;
import com.example.restaurateur.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifyRestInfoActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST = 100;
    private TextView tvInfoMessage;
    private EditText etEditInfo;
    private Button btnOk;
    private Button btnCancel;
    private String fieldName;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private static final String restaurantDataFile = "RestaurantDataFile";
    private String restaurantKey;

    private GeoPoint geo_address;
    private CompletableFuture<GeoPoint> completableFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modify_rest_info);

        SharedPreferences sharedPref = getSharedPreferences(restaurantDataFile, Context.MODE_PRIVATE);
        restaurantKey = sharedPref.getString("restaurantKey","");

        tvInfoMessage = findViewById(R.id.textViewTypeInfoRest);
        etEditInfo = findViewById(R.id.editTextChangeInfoRest);
        btnOk = findViewById(R.id.buttonOkRest);
        btnCancel = findViewById(R.id.buttonCancelRest);
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
            case "rest_name":
                title = getString(R.string.default_rest_name);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_restname);
                etEditInfo.setText(fieldValue);
                etEditInfo.setHint("Restaurant name");
                etEditInfo.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                etEditInfo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                //etEditInfo.setSelectAllOnFocus(true);
                etEditInfo.selectAll();
                break;
            case "rest_description":
                title = getString(R.string.rest_description);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_rest_description);
                etEditInfo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                etEditInfo.setText(fieldValue);
                etEditInfo.selectAll();
                break;
            case "rest_phone_number":
                title = getString(R.string.phone_number_title);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_rest_phone_number);
                etEditInfo.setText(fieldValue);
                etEditInfo.setInputType(InputType.TYPE_CLASS_PHONE);
                etEditInfo.selectAll();
                break;
             case "rest_address":
                title = getString(R.string.restaurant_address);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_rest_address);
                etEditInfo.setText(fieldValue);
                etEditInfo.setInputType(InputType.TYPE_CLASS_TEXT);
                etEditInfo.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
                etEditInfo.selectAll();
                break;
            case "delivery_fee":
                title = getString(R.string.delivery_fee_title);
                getSupportActionBar().setTitle(title);
                tvInfoMessage.setText(R.string.insert_delivery_fee);
                etEditInfo.setText(fieldValue);
                etEditInfo.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
               etEditInfo.setCompoundDrawables( getResources().getDrawable(R.drawable.ic_account_fee), null, null, null );
                etEditInfo.selectAll();
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
            Intent intent;
            Bundle Bn;
            String restName,restDescr;
            String restPhoneNumber, restFee,restAddress;

            switch (fieldName) {
                case "rest_name":
                    restName = etEditInfo.getText().toString();
                    if (!restName.equals("")) {
                        Map<String, Object> rest_name = new HashMap<>();
                        rest_name.put("rest_name", restName);
                        db.collection("restaurant").document(restaurantKey).update(rest_name)
                                .addOnSuccessListener(task -> {
                                    Intent retIntent;
                                    Bundle bn;
                                    Toast.makeText(ModifyRestInfoActivity.this, getString(R.string.username_updated), Toast.LENGTH_LONG).show();
                                    retIntent = new Intent(getApplicationContext(), RestInformationActivity.class);
                                    bn = new Bundle();
                                    bn.putString("field", fieldName);
                                    bn.putString("value", etEditInfo.getText().toString());
                                    retIntent.putExtras(bn);
                                    setResult(RESULT_OK, retIntent);
                                    finish();
                                })
                                .addOnFailureListener((task -> {
                                    Log.d("ModifyInfoRest", "failed update restaurant name");

                                    Toast.makeText(ModifyRestInfoActivity.this, getString(R.string.rest_name_failed_update), Toast.LENGTH_LONG).show();
                                }));
                    } else {
                        Toast mioToast = Toast.makeText(ModifyRestInfoActivity.this,
                                getString(R.string.invalid_rest_name),
                                Toast.LENGTH_LONG);
                        mioToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 64);
                        InputMethodManager imm12 = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm12.showSoftInput(etEditInfo, InputMethodManager.SHOW_IMPLICIT);
                        mioToast.show();

                        etEditInfo.selectAll();
                    }
                    break;
                case "rest_description":
                    restDescr = etEditInfo.getText().toString();
                    if (!restDescr.equals("")) {
                        Map<String, Object> rest_descr= new HashMap<>();
                        rest_descr.put("rest_descr", restDescr);
                        db.collection("restaurant").document(restaurantKey).update(rest_descr)
                                .addOnSuccessListener(task -> {
                                    Intent retIntent;
                                    Bundle bn;
                                    Toast.makeText(ModifyRestInfoActivity.this, getString(R.string.description_updated), Toast.LENGTH_LONG).show();
                                    retIntent = new Intent(getApplicationContext(), RestInformationActivity.class);
                                    bn = new Bundle();
                                    bn.putString("field", fieldName);
                                    bn.putString("value", etEditInfo.getText().toString());
                                    retIntent.putExtras(bn);
                                    setResult(RESULT_OK, retIntent);
                                    finish();
                                })
                                .addOnFailureListener((task -> {
                                    Log.d("ModifyInfoRest", "failed update restaurant description");

                                    Toast.makeText(ModifyRestInfoActivity.this, getString(R.string.rest_descr_failed_update), Toast.LENGTH_LONG).show();
                                }));
                    } else {
                        Toast mioToast = Toast.makeText(ModifyRestInfoActivity.this,
                                getString(R.string.invalid_rest_descr),
                                Toast.LENGTH_LONG);
                        mioToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 64);
                        InputMethodManager imm12 = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm12.showSoftInput(etEditInfo, InputMethodManager.SHOW_IMPLICIT);
                        mioToast.show();

                        etEditInfo.selectAll();
                    }
                    break;
                case "rest_address":
                    restAddress = etEditInfo.getText().toString();
                    if (!restAddress.equals("")) {
                        Map<String, Object> rest_address = new HashMap<>();
                        rest_address.put("rest_address", restAddress);

                        int res = check_GPS();
                        if(res == 0){
                            // TODO - MARCO usare quest
                            // progressBar.setVisibility(View.VISIBLE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                completableFuture = new CompletableFuture<>();
                            }

                            GeocodingLocation locationAddress = new GeocodingLocation();
                            locationAddress.getAddressFromLocation(restAddress,
                                    this, new ModifyRestInfoActivity.GeocoderHandler(), completableFuture);
                        }
                        try {
                            geo_address = completableFuture.get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        rest_address.put("rest_position", geo_address);

                        db.collection("restaurant").document(restaurantKey).update(rest_address)
                                .addOnSuccessListener((task -> {
                                    // TODO - MARCO qua
                                    // progressBar.setVisibility(View.GONE);
                                    Intent retIntent;
                                    Bundle bn;
                                    Toast.makeText(ModifyRestInfoActivity.this, getString(R.string.address_updated), Toast.LENGTH_LONG).show();
                                    retIntent = new Intent(getApplicationContext(), RestInformationActivity.class);
                                    bn = new Bundle();
                                    bn.putString("field", fieldName);
                                    bn.putString("value", etEditInfo.getText().toString());
                                    retIntent.putExtras(bn);
                                    setResult(RESULT_OK, retIntent);
                                    finish();
                                }))
                                .addOnFailureListener(task -> {
                                    Log.d("ModifyRestInfo", "Failed update rest address");
                                    Toast.makeText(ModifyRestInfoActivity.this, getString(R.string.address_failed_updated), Toast.LENGTH_LONG).show();
                                    etEditInfo.selectAll();
                                });

                    } else {
                        Toast mioToast = Toast.makeText(ModifyRestInfoActivity.this,
                                getString(R.string.invalid_address),
                                Toast.LENGTH_LONG);
                        mioToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 64);
                        InputMethodManager imm12 = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm12.showSoftInput(etEditInfo, InputMethodManager.SHOW_IMPLICIT);
                        mioToast.show();

                        etEditInfo.selectAll();
                    }
                    break;
                case "rest_phone_number":
                    restPhoneNumber = etEditInfo.getText().toString();
                    if (isValidPhone(restPhoneNumber)) {
                        Map<String, Object> rest_phone = new HashMap<>();
                        rest_phone.put("rest_phone", restPhoneNumber);
                        db.collection("restaurant").document(restaurantKey).update(rest_phone)
                                .addOnSuccessListener((task -> {
                                    Intent retIntent;
                                    Bundle bn;
                                    Toast.makeText(ModifyRestInfoActivity.this, getString(R.string.phone_updated), Toast.LENGTH_LONG).show();
                                    retIntent = new Intent(getApplicationContext(), RestInformationActivity.class);
                                    bn = new Bundle();
                                    bn.putString("field", fieldName);
                                    bn.putString("value", etEditInfo.getText().toString());
                                    retIntent.putExtras(bn);
                                    setResult(RESULT_OK, retIntent);
                                    finish();
                                }))
                                .addOnFailureListener(task -> {
                                    Log.d("MOdifyRestInfo", "Failed update phone");
                                    Toast.makeText(ModifyRestInfoActivity.this, getString(R.string.phone_failed_updated), Toast.LENGTH_LONG).show();
                                    etEditInfo.selectAll();
                                });

                    } else {
                        Toast mioToast = Toast.makeText(ModifyRestInfoActivity.this,
                                getString(R.string.invalid_phone),
                                Toast.LENGTH_LONG);
                        mioToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 64);
                        InputMethodManager imm12 = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm12.showSoftInput(etEditInfo, InputMethodManager.SHOW_IMPLICIT);
                        mioToast.show();

                        etEditInfo.selectAll();
                    }
                    break;
                case "delivery_fee":
                    restFee = etEditInfo.getText().toString();
                    if (!restFee.equals("")) {
                        Map<String, Object> rest_fee = new HashMap<>();
                        rest_fee.put("delivery_fee", restFee);
                        db.collection("restaurant").document(restaurantKey).update(rest_fee)
                                .addOnSuccessListener((task -> {
                                    Intent retIntent;
                                    Bundle bn;
                                    Toast.makeText(ModifyRestInfoActivity.this, getString(R.string.fee_updated), Toast.LENGTH_LONG).show();
                                    retIntent = new Intent(getApplicationContext(), RestInformationActivity.class);
                                    bn = new Bundle();
                                    bn.putString("field", fieldName);
                                    bn.putString("value", etEditInfo.getText().toString());
                                    retIntent.putExtras(bn);
                                    setResult(RESULT_OK, retIntent);
                                    finish();
                                }))
                                .addOnFailureListener(task -> {
                                    Log.d("ModifyRestInfo", "Failed update delivery fee");
                                    Toast.makeText(ModifyRestInfoActivity.this, getString(R.string.fee_failed_updated), Toast.LENGTH_LONG).show();
                                    etEditInfo.selectAll();
                                });

                    } else {
                        Toast mioToast = Toast.makeText(ModifyRestInfoActivity.this,
                                getString(R.string.invalid_fee),
                                Toast.LENGTH_LONG);
                        mioToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 64);
                        InputMethodManager imm12 = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm12.showSoftInput(etEditInfo, InputMethodManager.SHOW_IMPLICIT);
                        mioToast.show();

                        etEditInfo.selectAll();
                    }
                    break;

            }

        });

        btnCancel.setOnClickListener(v -> finish());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean isValidPhone(final String phone) {

        Pattern pattern;
        Matcher matcher;

        final String PHONE_PATTERN = "^[+]?[0-9]{10,13}$";

        pattern = Pattern.compile(PHONE_PATTERN);
        matcher = pattern.matcher(phone);

        return matcher.matches();

    }

    private int check_GPS(){
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Snackbar.make(findViewById(R.id.modrestinfo), "Please active GPS!",
                    Snackbar.LENGTH_LONG).show();
            return 1;
        }

        //Check whether this app has access to the location permission//
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        //If the location permission has been granted, then start the TrackerService//
        if (permission == PackageManager.PERMISSION_GRANTED) {
            return 0;
        } else {
            //If the app doesn’t currently have access to the user’s location, then request access//
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
            return 2;
        }
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            GeoPoint geo = null;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            if(locationAddress != null){
                String lat = locationAddress.split(",")[0];
                String lon = locationAddress.split(",")[1];

                // geo = new GeoPoint(Double.parseDouble(lat), Double.parseDouble(lon));
            }

            // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //     completableFuture.complete(geo);
            // }


        }
    }

}
