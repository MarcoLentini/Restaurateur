package com.example.restaurateur.Offer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.restaurateur.MainActivity;
import com.example.restaurateur.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

import static android.support.constraint.Constraints.TAG;

public class AddNewOfferActivity extends AppCompatActivity {

    private TextInputLayout textInputFoodName, textInputFoodPrice, textInputFoodQuantity, textInputFoodDescription;
    private EditText etFoodName, etFoodPrice, etFoodQuantity, etFoodDescription;
    private Button btnCancel, btnSave;
    private View image_button;
    private Category category;
    // private ProgressBar progressBar;
    private FirebaseAuth auth;

    private static final int CAMERA_REQUEST = 2;
    private static final int GALLERY_REQUEST = 3;
    private static final int STORAGE_PERMISSION_CODE = 4;
    private static final int CAMERA_PERMISSION_CODE = 5;

    private Uri offer_image = null;
    private Uri file_image = null;
    private static final String AuthorityFormat = "%s.fileprovider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_offer_item);
        Intent receivedIntent = getIntent();

        int categoryPosition =  receivedIntent.getExtras().getInt("category");
        category = MainActivity.categoriesData.get(categoryPosition);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        String title = getString(R.string.title_new_food);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        image_button = findViewById(R.id.background_img_offer);
        image_button.setOnClickListener(v-> invokeDialogImageProfile() );

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
        btnCancel.setOnClickListener(v -> finish());
        btnSave = findViewById(R.id.etOfferBtnSave);
        btnSave.setOnClickListener(v -> {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            if(validateFoodInput()) {
                Intent retIntent = new Intent(getApplicationContext(), OffersDishFragment.class);
                Bundle bn = new Bundle();
                bn.putString("category", category.getCategoryName());
                bn.putString("foodName", etFoodName.getText().toString());
                bn.putDouble("foodPrice", Double.parseDouble(etFoodPrice.getText().toString()));
                bn.putLong("foodQuantity", Long.parseLong(etFoodQuantity.getText().toString()));
                bn.putString("foodDescription", etFoodDescription.getText().toString());
                bn.putString("foodImage", offer_image.toString());
                retIntent.putExtras(bn);
                setResult(RESULT_OK, retIntent);
                finish();
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

        if(offer_image == null) {
            Toast.makeText(getApplicationContext(), "Insert an image or retry", Toast.LENGTH_LONG);
            return false;
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == CAMERA_REQUEST)  {
                uploadOnFirebase(file_image);
            }
            if(requestCode == GALLERY_REQUEST){
                uploadOnFirebase(data.getData());
            }
        }
    }

    private void uploadOnFirebase(Uri fileUri){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference photoRef = storageRef.child("photos/" + auth.getCurrentUser().getUid() + "/offers/")
                .child(UUID.randomUUID().toString());

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
            offer_image = downloadUri;
            Glide.with(this).load(offer_image).placeholder(R.drawable.dish_pic).into((ImageView) findViewById(R.id.offer_food_pic));
            try {
                deleteImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//        // progressBar.setVisibility(View.GONE);
//    }

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
}
