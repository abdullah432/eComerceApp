package com.example.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextView username,email_address,pass,confirmPass;

    private ProgressDialog progressDialog;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.registerButton);

        username = findViewById(R.id.user_name);
        email_address = findViewById(R.id.email_address);
        pass = findViewById(R.id.password);
        confirmPass = findViewById(R.id.confirmPass);
        progressDialog = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = username.getText().toString();
                String email = email_address.getText().toString();
                String password = pass.getText().toString();
                String confirmPassword = confirmPass.getText().toString();

                if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword))
                    Toast.makeText(RegisterActivity.this,"Some fields are empty",Toast.LENGTH_LONG).show();
                else if(!password.equals(confirmPassword))
                    Toast.makeText(RegisterActivity.this,"Confirm password is incorrect",Toast.LENGTH_LONG).show();
                else
                {
                    progressDialog.setTitle("Creating Account");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Creating Account is in Progress");
                    progressDialog.show();

                    validateNewRequest(userName,email,password);
                }
            }
        });
    }

    private void validateNewRequest(final String userName, final String email, final String password) {
        final String encodeEmail = email.replace(".",",");
        final DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(encodeEmail).exists())){
                    HashMap<String,Object> userData = new HashMap<>();
                    userData.put("name",userName);
                    userData.put("email",email);
                    userData.put("password",password);

                    databaseReference.child("Users").child(encodeEmail).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Account is created Successfully",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this,"Already Register",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
