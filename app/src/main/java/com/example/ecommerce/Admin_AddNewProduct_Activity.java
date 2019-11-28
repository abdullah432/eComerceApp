package com.example.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;


public class Admin_AddNewProduct_Activity extends AppCompatActivity {

    private String categoryName, saveCurrentDate, saveCurrentTime, uniqueImageKey, downloadProductURL;

    private static final int galleryImagePickCode = 1;

    private ImageView select_Product_Image;

    private EditText newProductName, newProductDescription, newProductPrice;

    private Uri imageUri;

    private String productName, productPrice, productDescription;

    private StorageReference productImageRef;

    private DatabaseReference databaseReference;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__add_new_product_);

        categoryName = getIntent().getExtras().get("category").toString();

//        Toast.makeText(this,categoryName,Toast.LENGTH_LONG).show();

        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");

        select_Product_Image = findViewById(R.id.select_Product_Image);

        select_Product_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,galleryImagePickCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == galleryImagePickCode && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            select_Product_Image.setImageURI(imageUri);
        }
    }

    public void addProduct(View view){

        newProductName = findViewById(R.id.newProductName);
        newProductDescription = findViewById(R.id.newProductDescription);
        newProductPrice = findViewById(R.id.newProductPrice);

        productName = newProductName.getText().toString();
        productPrice = newProductPrice.getText().toString();
        productDescription = newProductDescription.getText().toString();

        if(TextUtils.isEmpty(productName))
            Toast.makeText(this,"Product Name is missing",Toast.LENGTH_SHORT).show();
        else if(TextUtils.isEmpty(productPrice))
                Toast.makeText(this,"Product Price is missing",Toast.LENGTH_SHORT).show();
        else if(TextUtils.isEmpty(productDescription))
            Toast.makeText(this,"Product Description is missing",Toast.LENGTH_SHORT).show();
        else if(imageUri == null)
            Toast.makeText(this,"Please select image for Product",Toast.LENGTH_SHORT).show();
        else {

            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Adding Product...");
            progressDialog.setMessage("Wait for a second");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            //Create a unique key
            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");

            saveCurrentDate = currentDate.format(calendar.getTime());
            saveCurrentTime = currentTime.format(calendar.getTime());

            uniqueImageKey = saveCurrentDate +" "+saveCurrentTime;

            //Storing image in Firebase storage
            final StorageReference filePath = productImageRef.child(uniqueImageKey);

            final UploadTask uploadTask = filePath.putFile(imageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();

                    String message = e.toString();
                    Toast.makeText(Admin_AddNewProduct_Activity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(Admin_AddNewProduct_Activity.this,"Image is Uploaded successfully",Toast.LENGTH_SHORT).show();

                    //getting image url
                    Task<Uri>  taskURL = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){

                                progressDialog.dismiss();

                                throw task.getException();

                            }

                            /*//filpath uri
                            downloadProductURL = filePath.getDownloadUrl().toString();*/

                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if(task.isSuccessful()){

                                //To get URL of image
                                downloadProductURL = task.getResult().toString();

                                saveProductInfoInDatabase();
                            }

                        }
                    });
                }
            });

        }
    }

    private void saveProductInfoInDatabase() {
        HashMap<String,Object> productInfo = new HashMap<>();
        productInfo.put("pid",uniqueImageKey);
        productInfo.put("product_name",productName);
        productInfo.put("description",productDescription);
        productInfo.put("price",productPrice);
        productInfo.put("image",downloadProductURL);
        productInfo.put("category",categoryName);
        productInfo.put("date",saveCurrentDate);
        productInfo.put("time",saveCurrentTime);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Products").child(uniqueImageKey).updateChildren(productInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();

                    Toast.makeText(Admin_AddNewProduct_Activity.this,"Product is added Successfully",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Admin_AddNewProduct_Activity.this,Admin_Category_Activity.class);
                    startActivity(intent);
                }
                else {
                    progressDialog.dismiss();
                    String message = Objects.requireNonNull(task.getException()).toString();
                    Toast.makeText(Admin_AddNewProduct_Activity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
