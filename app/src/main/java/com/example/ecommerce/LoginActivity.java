package com.example.ecommerce;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.MyClasses.Users;
import com.example.ecommerce.RememberMe.RemeberMe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

import static com.example.ecommerce.RememberMe.RemeberMe.currentOnlineUser;

public class LoginActivity extends AppCompatActivity {

    private CheckBox checkBox;

    private TextView email,password;

    private ProgressBar progressBar;

    private String primaryDatabaseName = "Users";

    private TextView admin_login;
    private TextView not_admin;

    private Button singinButton;

    void checkUserAccount(final String email,final String password){

        if(checkBox.isChecked()){

            Paper.book().write(RemeberMe.userEmailKey,email);
            Paper.book().write(RemeberMe.userPasswordKey,password);

        }

        progressBar.setVisibility(View.VISIBLE);


        final String emailTextEncode = email.replace(".",",");

        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(primaryDatabaseName).child(emailTextEncode).exists()){

                    Users userData = dataSnapshot.child(primaryDatabaseName).child(emailTextEncode).getValue(Users.class);

                    if(primaryDatabaseName.equals("Users")){
                        if(userData.getEmail().equals(email)){
                            if(userData.getPassword().equals(password)){

                                progressBar.setVisibility(View.INVISIBLE);
                                currentOnlineUser = userData;

                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(intent);

                            }
                            else {
                                Toast.makeText(LoginActivity.this,"Wrong Password",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"Wrong Email Address. Please Enter Correct Email Address",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }
                    else if(primaryDatabaseName.equals("Admins")){
                        if(userData.getEmail().equals(email)){
                            if(userData.getPassword().equals(password)){

                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(LoginActivity.this, Admin_Category_Activity.class);
                                startActivity(intent);

                            }
                            else {
                                Toast.makeText(LoginActivity.this,"Wrong Password",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"Wrong Email Address. Please Enter Correct Email Address",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                }
                else {
                    Toast.makeText(LoginActivity.this,"Incorrect Email Address",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Paper.init(this);

        email = findViewById(R.id.signIn_input_editEmail);
        password = findViewById(R.id.signIn_input_password);
        progressBar = findViewById(R.id.progressBar);
        checkBox = findViewById(R.id.rememberMe_checkBox);

        admin_login = findViewById(R.id.admin_login);
        not_admin = findViewById(R.id.not_admin);

        singinButton = findViewById(R.id.signinButton);

        singinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String emailText = email.getText().toString();
                final String passwordText = password.getText().toString();

                if(TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText))
                    Toast.makeText(LoginActivity.this,"Some fields are empty",Toast.LENGTH_LONG).show();
                else
                {
                    checkUserAccount(emailText,passwordText);
                }

            }
        });

        admin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin_login.setVisibility(View.INVISIBLE);
                not_admin.setVisibility(View.VISIBLE);

                primaryDatabaseName = "Admins";

            }
        });

        not_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin_login.setVisibility(View.VISIBLE);
                not_admin.setVisibility(View.INVISIBLE);

                primaryDatabaseName ="Users";
            }
        });

    }
}
