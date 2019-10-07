package com.example.restaurateur.Information;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.restaurateur.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class RestInformationActivity extends AppCompatActivity {
    private static final int TIMETABLE = 6;
    private TextView tvRestAddress,tvRestName,tvRestDescr,tvRestTags;
    private TextView tvOpeningHours,tvRestPhoneNumber,tvRemoveRest;
    private TextView tvDeliveryFee;
    private String restAddress;

    private String openingHours;
    private Double deliveryFee;

    private ImageView imageProfile;
    private Uri uriSelectedImage;

    private String RestName;
    private String RestAddress;
    private String RestPhoneNumber;

    private SharedPreferences sharedPref;
    private static final String restaurantFile = "RestaurantDataFile";

    private Uri restaurant_image = null;
    private Uri file_image = null;
    private RestInformationModel restInfo;
    private static final int SECOND_ACTIVITY = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int GALLERY_REQUEST = 3;
    private static final int STORAGE_PERMISSION_CODE = 4;
    private static final int CAMERA_PERMISSION_CODE = 5;
    private static final String AuthorityFormat = "%s.fileprovider";
    private static final int MAX_RESTAURANT_TYPES=3;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String restaurantKey;
    private static final String RestaurantDataFile = "RestaurantDataFile";

    private String restDescr;
    private String restPhoneNumber;

    private int countChecked;
    private ArrayList<Integer> selectedRestaurantTypes;
    private String[] restaurantTypes = {"Pizza", "Cinese", "Giapponese", "Indiano", "Italiano", "Hamburger",
            "Pasta", "Greca", "Panini", "Dolci", "Americano", "Argentino", "Brasiliano", "Messicano",
            "Insalate", "Kebab", "Piadine", "Spagnolo", "Thailandese", "Vegetariano", "Senza Glutine",
            "Gelato"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.waiting_view);

        String titile = getString(R.string.RestInfoTitle);
        getSupportActionBar().setTitle(titile);
        SharedPreferences sharedPref = getSharedPreferences(RestaurantDataFile, Context.MODE_PRIVATE);
        restaurantKey = sharedPref.getString("restaurantKey","");

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null || restaurantKey.equals("")) {
            finish();
        }

//Get Firestore instance
        db = FirebaseFirestore.getInstance();

        db.collection("restaurant").document(restaurantKey).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {

                            Map<String, Boolean> receivedTags;
                            ArrayList<String> tags = null;
                            if(doc.get("tags") != null) {
                                receivedTags = (Map<String, Boolean>)doc.get("tags");
                                tags = new ArrayList<>(receivedTags.keySet());
                            }
                                restInfo = new RestInformationModel(
                                         doc.getString("rest_name"),
                                         doc.getString("rest_descr"),
                                         doc.getString("rest_address"),
                                         doc.getString("rest_phone"),
                                         doc.getDouble("delivery_fee"),
                                         doc.getString("user_id"),
                                        Uri.parse( doc.getString("rest_image")),
                                        tags,
                                        (HashMap<String,Object>)doc.get("timetable"),
                                        doc.getGeoPoint("rest_position")
                                );


                            setContentView(R.layout.activity_main_viewmore);

                            imageProfile = findViewById(R.id.img_profile_rest);
                            ImageView imageAddButton = findViewById(R.id.background_img_rest_info);
                            imageAddButton.setOnClickListener(v -> {
                                invokeDialogImageProfile();
                            });
                            tvRestName = findViewById(R.id.textViewRestName);
                            tvRestName.setOnClickListener(v -> {
                                String idField ="rest_name";
                                invokeModifyInfoActivity(idField,RestName);
                            });

                            tvRestDescr = findViewById(R.id.textViewRestDescription);
                            tvRestDescr.setOnClickListener(v -> {
                                String idField ="rest_description";
                                invokeModifyInfoActivity(idField,restDescr);
                            });

                            tvRestTags = findViewById(R.id.textViewRestTags);
                            tvRestTags.setOnClickListener(v -> {
                                selectRestaurantType();
                            });

                            tvRestAddress = findViewById(R.id.textViewRestAddress);
                            tvRestAddress.setOnClickListener(v -> {
                                String idField ="rest_address";
                                invokeModifyInfoActivity(idField,restAddress);
                            });

                            tvOpeningHours = findViewById(R.id.textViewRestTimetable);
                            tvOpeningHours.setOnClickListener(v -> {
                                Intent intent =new Intent(getApplicationContext(), EditTimeTableActivity.class);
                                Bundle args = new Bundle();
                                args.putSerializable("timetable", restInfo.getTimetable());
                                intent.putExtras(args);
                                startActivityForResult(intent,TIMETABLE);
                            });

                            tvRestPhoneNumber = findViewById(R.id.textViewRestPhoneNumber);
                            tvRestPhoneNumber.setOnClickListener(v -> {
                                String idField ="rest_phone_number";
                                invokeModifyInfoActivity(idField,restPhoneNumber);
                            });

                            tvDeliveryFee = findViewById(R.id.textViewDeliveryFee);
                            tvDeliveryFee.setOnClickListener(v -> {
                                String idField = "delivery_fee";
                                invokeModifyInfoActivity(idField,deliveryFee.toString());
                            });

                            tvRemoveRest=findViewById(R.id.textViewRemoveRest);
                            tvRemoveRest.setOnClickListener(v -> {
                                if (user != null) {
                                    db.collection("restaurant").document(restaurantKey).delete()
                                            .addOnSuccessListener(taskBikerId -> {
                                                db.collection("users").whereEqualTo("rest_id",restaurantKey).get()
                                                        .addOnSuccessListener(document->{
                                                            SharedPreferences.Editor editor = sharedPref.edit();
                                                            editor.remove("restaurantKey").apply();
                                                            Toast.makeText(RestInformationActivity.this, "Your restaurant profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                                            signOut();
                                                            finish();
                                                        })
                                                        .addOnFailureListener(taskFailId -> {
                                                            Log.d("RestInfo", "failed delete restKey");
                                                            Toast.makeText(RestInformationActivity.this, getString(R.string.failed_delete_rest), Toast.LENGTH_SHORT).show();
                                                        });

                                            })
                                            .addOnFailureListener(taskFailId -> {
                                                Log.d("BikerInfo", "failed delete restaurant");

                                                Toast.makeText(RestInformationActivity.this,  getString(R.string.failed_delete_rest), Toast.LENGTH_SHORT).show();
                                            });

                                }
                            });

                            if(restInfo!=null) {
                                RestName = restInfo.getRest_name();
                                if (!RestName.equals(""))
                                    tvRestName.setText(RestName);
                                restDescr=restInfo.getRest_descr();
                                restAddress = restInfo.getRest_address();
                                if (!restAddress.equals(""))
                                    tvRestAddress.setText(restAddress);
                               restPhoneNumber = restInfo.getRest_phone();
                                if (!restPhoneNumber.equals(""))
                                    tvRestPhoneNumber.setText(restPhoneNumber);
                                deliveryFee=restInfo.getDelivery_fee();
                                uriSelectedImage = restInfo.getRest_image();
                                if(uriSelectedImage!=null) {
                                    Glide.with(this).load(uriSelectedImage).placeholder(R.drawable.img_rest_1).into((ImageView) findViewById(R.id.img_profile_rest));
                                    try {
                                        deleteImage();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        } else {
                            Log.d("QueryReservation", "No such document");
                        }
                        } else {
                            Log.d("QueryReservation", "get failed with ", task.getException());
                        }

                });

    }
    private void invokeModifyInfoActivity(String fieldName, String fieldNameValue){
        Intent intent = new Intent(getApplicationContext(), ModifyRestInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("field", fieldName);
        bundle.putString("value", fieldNameValue);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                uploadOnFirebase(file_image);
            } else if (requestCode == GALLERY_REQUEST) {
                uploadOnFirebase(data.getData());
            } else {
                switch (data.getExtras().getString("field")) {
                    case "rest_name":
                        RestName = data.getExtras().getString("value");
                        if (!RestName.equals("")) {
                            restInfo.setRest_name(RestName);
                            tvRestName.setText(RestName);
                        }
                        break;
                    case "rest_description":
                        restDescr = data.getExtras().getString("value");
                        if (!restDescr.equals("")) {
                            restInfo.setRest_name(restDescr);
                        }
                        break;
                    case "rest_address":
                        restAddress = data.getExtras().getString("value");
                        if (!restAddress.equals("")) {
                            restInfo.setRest_address(restAddress);
                            tvRestAddress.setText(restAddress);
                        }
                        break;
                    case "rest_phone_number":
                        restPhoneNumber = data.getExtras().getString("value");
                        if (!restPhoneNumber.equals("")) {
                            restInfo.setRest_phone(restPhoneNumber);
                            tvRestPhoneNumber.setText(restPhoneNumber);
                        }
                        break;

                    case "delivery_fee":
                        deliveryFee = Double.parseDouble(data.getExtras().getString("value"));
                        if (!deliveryFee.equals("")) {
                            restInfo.setDelivery_fee(deliveryFee);
                        }
                        break;

                }
            }
        }
        if(resultCode==2)
        {
                restInfo.setTimetable((HashMap<String,Object>) data.getExtras().getSerializable("timetable"));
                int ciao=1;
                ciao++;

        }
    }

    private void invokeDialogImageProfile(){
        final String[] items = { getString(R.string.take_a_picture), getString(R.string.pick_from_gallery), getString(R.string.cancel_string)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_photo));
        builder.setItems(items, (d, i) -> {
            if (items[i].equals(getString(R.string.take_a_picture))) {
                invokeTakePicture();
            } else if (items[i].equals(getString(R.string.pick_from_gallery))) {
                invokeGallery();
            } else if (items[i].equals(getString(R.string.cancel_string))) {
                d.dismiss();
            }
        });
        builder.show();
    }

    private void invokeTakePicture(){
        if(hasPermission("Camera")){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getPackageManager()) != null){
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e("[Camera Error]", ex.getMessage());
                }

                if(photoFile != null){
                    String authority = String.format(Locale.getDefault(), AuthorityFormat, this.getPackageName());
                    file_image = FileProvider.getUriForFile(this, authority, photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, file_image);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent, CAMERA_REQUEST);
                }
            }
        }
    }

    private void invokeGallery(){
        if (hasPermission("Storage")) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY_REQUEST);
        }
    }


    private void uploadOnFirebase(Uri fileUri){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference photoRef = storageRef.child("photos/" + auth.getCurrentUser().getUid())
                .child("ImageProfile.jpg");

        photoRef.putFile(fileUri).continueWithTask(task -> {
            // Forward any exceptions
            if (!task.isSuccessful())
                throw task.getException();

            Log.d(TAG, "uploadFromUri: upload success");

            // Request the public download URL
            return photoRef.getDownloadUrl();
        }).addOnSuccessListener(downloadUri -> {
            // Upload succeeded
            Log.d(TAG, "uploadFromUri: getDownloadUri success");
            restaurant_image = downloadUri;
            Glide.with(this).load(restaurant_image).placeholder(R.drawable.img_rest_1).into((ImageView) findViewById(R.id.img_profile_rest));
            try {
                deleteImage();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Map<String, Object> rest_im = new HashMap<>();
            rest_im.put("rest_image", restaurant_image.toString());
            restInfo.setRest_image(restaurant_image);
            db.collection("restaurant").document(restaurantKey).update(rest_im);

        }).addOnFailureListener(exception -> {
            // Upload failed
            Log.w(TAG, "uploadFromUri:onFailure", exception);
        });
    }


    public void deleteImage() throws IOException {
        File fdelete = createImageFile();
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                callBroadCast();
            } else {
            }
        }
    }

    private void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStorageDirectory().toString()}, null, (path, uri) -> {
                Log.e("ExternalStorage", "Scanned " + path + ":");
                Log.e("ExternalStorage", "-> uri=" + uri);
            });
        } else {
            Log.e("-->", " < 14");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }


    /** Permission Function **/

    // Image profile still valid?
    private boolean imageProfileIsValid() {
        File img_prof = null;
        try {
            img_prof = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img_prof.exists();
    }
    // For Image Profile -- URI
    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "ImageProfile";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir + File.separator + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        //currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean hasPermission(String perm){
        if (Build.VERSION.SDK_INT >= 23) {
            if(perm.equals("Storage")) {
                if (checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    return true;
                else
                    requestStoragePermission("Gallery");
            } else if(perm.equals("Camera")){
                if (checkPermission(android.Manifest.permission.CAMERA) && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    return true;
                else
                    requestCameraPermission();
            }
        }
        else {
            return true;
        }
        return false;
    }

    private boolean checkPermission(String perm) {
        int result = ContextCompat.checkSelfPermission(this, perm);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission(String type) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.perm_needed))
                    .setMessage(getString(R.string.perm_why_1))
                    .setPositiveButton(getString(R.string.ok_string), (dialog, which) -> ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE))
                    .setNegativeButton(getString(R.string.cancel_string), (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    private void requestCameraPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.perm_needed))
                    .setMessage(getString(R.string.perm_why_2))
                    .setPositiveButton(getString(R.string.ok_string), (dialog, which) -> ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE))
                    .setNegativeButton(getString(R.string.cancel_string), (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    invokeGallery();
                else
                    Toast.makeText(this, getString(R.string.perm_denied), Toast.LENGTH_SHORT).show();
                break;
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    invokeTakePicture();
                else
                    Toast.makeText(this, getString(R.string.perm_denied), Toast.LENGTH_SHORT).show();
                break;
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //sign out method
    public void signOut() {
        auth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (auth.getCurrentUser() == null || restaurantKey.equals("")) {

            finish();

        }
    }

    private void selectRestaurantType() {
        countChecked = 0;
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose restaurant tags(minimum 1 and maximum 3 tags)");
       boolean[] selectedRestTmp=new boolean[restaurantTypes.length];
        ArrayList<Integer> selectedRestPos= new ArrayList<>();
        ArrayList<String> tags=restInfo.getTags();

           for(int j=0;j<restaurantTypes.length;j++)
               if(tags.contains(restaurantTypes[j])){
                   selectedRestTmp[j]=true;
                   selectedRestPos.add(j);
                   countChecked++;
               }else
                  // selectedRestTmp[j]=false;

        builder.setMultiChoiceItems(restaurantTypes,selectedRestTmp, (dialog, which, isChecked) -> {
            if(isChecked) {
                if (countChecked <= MAX_RESTAURANT_TYPES - 1) {
                    selectedRestPos.add(which);
                    selectedRestTmp[which]=true;
                    countChecked++;
                } else {
                    //selected[which] = false;
                   ((CheckedTextView)((AlertDialog) dialog).getListView().getChildAt(which)).setChecked(false);
                    selectedRestTmp[which]=false;


                    int ciao=1;
                }
            } else {
                if(selectedRestPos.contains(Integer.valueOf(which))) {
                    selectedRestPos.remove(Integer.valueOf(which));
                    selectedRestTmp[which] = false;
                    countChecked--;
                }
            }
        });
        // add OK and Cancel buttons
        builder.setPositiveButton("OK", (dialog, which) -> {

        });
        builder.setNegativeButton("Cancel", null);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                if(!selectedRestPos.isEmpty()) {
                    ArrayList<String> tagsArray = new ArrayList<>();
                    Map<String, Object> tagsMap = new HashMap<>();
                    for (int pos : selectedRestPos) {
                        tagsArray.add(restaurantTypes[pos]);
                        tagsMap.put(restaurantTypes[pos], true);
                    }
                    HashMap<String,Object> new_tags= new HashMap<String,Object>();
                    new_tags.put("tags",tagsMap);
                    db.collection("restaurant").document(restaurantKey).update(new_tags)
                            .addOnSuccessListener(task3->{
                                restInfo.setTags(tagsArray);
                                Toast.makeText(RestInformationActivity.this, getString(R.string.tags_updated), Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            })
                            .addOnFailureListener(task3->{
                                Log.d("ModifyRestInfo", "Failed update rest address");
                                Toast.makeText(RestInformationActivity.this, getString(R.string.tags_failed_updated), Toast.LENGTH_LONG).show();

                            });


                }else
                {
                    Toast.makeText(RestInformationActivity.this, getString(R.string.select_one_tags), Toast.LENGTH_SHORT).show();

                }
        });

    }

}