package com.example.ecommerce;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.ecommerce.MyClasses.Users;
import com.example.ecommerce.RememberMe.RemeberMe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

import static com.example.ecommerce.RememberMe.RemeberMe.currentOnlineUser;

public class MainActivity extends AppCompatActivity {

    Button joinbutton , loginbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);
        loginbutton = findViewById(R.id.loginButton);
        joinbutton = findViewById(R.id.joinButton);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        joinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });

        String userEmail = Paper.book().read(RemeberMe.userEmailKey);
        String userPassword = Paper.book().read(RemeberMe.userPasswordKey);

        if(!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPassword)){
            checkUserAccount(userEmail, userPassword);
        }

    }

    private void checkUserAccount(final String email, final String password) {

        final String emailEncode = email.replace(".",",");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Users").child(emailEncode).exists()){
                    Users userData = dataSnapshot.child("Users").child(emailEncode).getValue(Users.class);

                    if(userData.getEmail().equals(email)){

                        if(userData.getPassword().equals(password)){
                            currentOnlineUser = userData;
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }

                    }
                }
                else if(dataSnapshot.child("Admins").child(emailEncode).exists()){
                    Users userData = dataSnapshot.child("Admins").child(emailEncode).getValue(Users.class);

                    if(userData.getEmail().equals(email)){
                        if(userData.getPassword().equals(password)){
                            Intent intent = new Intent(MainActivity.this,Admin_Category_Activity.class);
                            startActivity(intent);
                        }
                        else {
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                    else {
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
