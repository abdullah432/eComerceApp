package com.example.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.MyClasses.Users;
import com.example.ecommerce.RememberMe.RemeberMe;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    EditText update_name, update_email, update_homeAddress;
    ImageView setting_profile_image;
    TextView setting_close_button, setting_update_button, updateProfile;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    Uri imageUri;

    private boolean imageSelected = false;

    private String useridentity;

    private String profileImageURLAddress;

    private ProgressDialog progressDialog;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        progressBar = findViewById(R.id.progressBar);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Images");

        update_name = findViewById(R.id.update_name);
        update_email = findViewById(R.id.update_email);
        update_homeAddress = findViewById(R.id.update_homeAddress);
        setting_profile_image = findViewById(R.id.setting_profile_image);
        setting_close_button = findViewById(R.id.setting_close_button);
        setting_update_button = findViewById(R.id.setting_update_button);
        updateProfile = findViewById(R.id.updateProfile);

        loadUserInfo();

        setting_close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);

            }
        });

        setting_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(imageUri)
                        .start(SettingsActivity.this);

            }
        });

        setting_update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imageSelected)
                    updateInfoImage();
                else
                    updateInfo();

            }
        });
    }

    private void updateInfoImage() {

        if(imageSelected){

            final StorageReference fileRef = storageReference.child(RemeberMe.currentOnlineUser.getEmail());

            final UploadTask uploadTask = fileRef.putFile(imageUri);

            progressDialog = new ProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setTitle("Updating Information...");
            progressDialog.setMessage("Wait for a second");
            progressDialog.show();

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        progressDialog.setCanceledOnTouchOutside(true);
                        progressDialog.dismiss();

                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri imageURL = task.getResult();
                        if(imageURL != null)
                            profileImageURLAddress = imageURL.toString();

                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("name",update_name.getText().toString());
                        hashMap.put("email",update_email.getText().toString());
                        hashMap.put("homeAddress",update_homeAddress.getText().toString());
                        hashMap.put("image",profileImageURLAddress);

                        databaseReference.child(useridentity).updateChildren(hashMap);
                        progressDialog.setCanceledOnTouchOutside(true);
                        progressDialog.dismiss();


                        Intent intent = new Intent(SettingsActivity.this,HomeActivity.class);
                        finish();
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(SettingsActivity.this,"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SettingsActivity.this,SettingsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }

    }

    private void updateInfo() {

        progressBar.setVisibility(View.VISIBLE);

        if(TextUtils.isEmpty(update_name.getText().toString())){
            Toast.makeText(SettingsActivity.this, "Name Field can't be empty",Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(update_email.getText().toString())){
            Toast.makeText(SettingsActivity.this, "Name Field can't be empty",Toast.LENGTH_SHORT).show();
        }else{

            progressBar.setVisibility(View.VISIBLE);
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("name",update_name.getText().toString());
            hashMap.put("email",update_email.getText().toString());
            hashMap.put("homeAddress",update_homeAddress.getText().toString());
            databaseReference.child(useridentity).updateChildren(hashMap);
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(SettingsActivity.this,"Update Successfully",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SettingsActivity.this,HomeActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK  && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageSelected = true;
            imageUri = result.getUri();
            setting_profile_image.setImageURI(imageUri);
        }
    }

    private void loadUserInfo() {

        useridentity = RemeberMe.currentOnlineUser.getEmail().replace(".",",");

        databaseReference.child(useridentity).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users userData = dataSnapshot.getValue(Users.class);

                update_name.setText(userData.getName());
                update_email.setText(userData.getEmail());
                update_homeAddress.setText(userData.getHomeAddress());
                if(!TextUtils.isEmpty(userData.getImage())){
                    Picasso.get().load(userData.getImage()).into(setting_profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
