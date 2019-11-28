package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.RememberMe.RemeberMe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class ConfirmActivity extends AppCompatActivity {

    private EditText confirmationName, confirmationPhoneNumber, confirmationHomeAddress, confirmationCityName;

    private String orderPrice;
    private int Price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        confirmationName = findViewById(R.id.confirmationName);
        confirmationPhoneNumber = findViewById(R.id.confirmationPhoneNumber);
        confirmationHomeAddress = findViewById(R.id.confirmationHomeAddress);
        confirmationCityName = findViewById(R.id.confirmationCityName);

        confirmationName.setText(RemeberMe.currentOnlineUser.getName());
        if(!TextUtils.isEmpty(RemeberMe.currentOnlineUser.getHomeAddress()))
            confirmationHomeAddress.setText(RemeberMe.currentOnlineUser.getHomeAddress());

        orderPrice = getIntent().getStringExtra("totalPrice");

        Log.i("orderPrice:",orderPrice);
    }

    public void confirmButton(View view){

        if(confirmationName.getText().toString().isEmpty()){
            Toast.makeText(ConfirmActivity.this,"Some field are empty",Toast.LENGTH_SHORT).show();

        }else if(confirmationPhoneNumber.getText().toString().isEmpty()){
            Toast.makeText(ConfirmActivity.this,"Some field are empty",Toast.LENGTH_SHORT).show();

        }else if(confirmationHomeAddress.getText().toString().isEmpty()){
            Toast.makeText(ConfirmActivity.this,"Some field are empty",Toast.LENGTH_SHORT).show();

        }else if(confirmationCityName.getText().toString().isEmpty()){
            Toast.makeText(ConfirmActivity.this,"Some field are empty",Toast.LENGTH_SHORT).show();

        }else {

            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat date = new SimpleDateFormat("MMM dd, yyyy");
            String currentDate = date.format(calendar.getTime());

            SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss a");
            String currentTime = time.format(calendar.getTime());

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            HashMap<String, Object> confirmHashMap = new HashMap<>();
            confirmHashMap.put("name", confirmationName.getText().toString());
            confirmHashMap.put("address", confirmationHomeAddress.getText().toString());
            confirmHashMap.put("contact", confirmationPhoneNumber.getText().toString());
            confirmHashMap.put("city", confirmationCityName.getText().toString());
            confirmHashMap.put("Price", orderPrice);
            confirmHashMap.put("State", "Not Shipped");
            confirmHashMap.put("Date", currentDate);
            confirmHashMap.put("Time", currentTime);
            confirmHashMap.put("userID", RemeberMe.currentOnlineUser.getEmail().replace(".",","));

            databaseReference.child("Orders").child(RemeberMe.currentOnlineUser.getEmail().replace(".",",")).updateChildren(confirmHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.i("Error",task.getException().toString());
                    }
                    else if(task.isSuccessful()){
                        databaseReference.child("Cart List").child("User View")
                                .child(RemeberMe.currentOnlineUser.getEmail().replace(".",",")).removeValue();
                        Toast.makeText(ConfirmActivity.this,"Orders Placed Successfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ConfirmActivity.this,HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
