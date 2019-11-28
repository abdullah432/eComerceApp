package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ecommerce.MyClasses.Products;
import com.example.ecommerce.RememberMe.RemeberMe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private String intentProductID;

    private  Products productDetail;

    private ImageView detail_product_image;
    private TextView product_detail_name;
    private TextView product_detail_description;
    private TextView product_detail_price;
    private Button addToCartButton;
    private ElegantNumberButton elegantNumberButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        intentProductID = getIntent().getStringExtra("pid");

        detail_product_image = findViewById(R.id.detail_product_image);
        product_detail_name = findViewById(R.id.product_detail_name);
        product_detail_description = findViewById(R.id.product_detail_description);
        product_detail_price = findViewById(R.id.product_detail_price);
        addToCartButton = findViewById(R.id.addToCartButton);
        elegantNumberButton = findViewById(R.id.elegantNumberButton);

        loadClickProductInfo();

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart List");

                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat date = new SimpleDateFormat("MMM dd, yyyy");
                String currenDate = date.format(calendar.getTime());

                SimpleDateFormat time = new SimpleDateFormat("MMM dd, yyyy");
                String currentTime = time.format(calendar.getTime());

                final HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("pid",intentProductID);
                hashMap.put("product_name",productDetail.getProduct_name());
                hashMap.put("price",productDetail.getPrice());
                hashMap.put("description",productDetail.getDescription());
                hashMap.put("category",productDetail.getCategory());
                hashMap.put("quantity",elegantNumberButton.getNumber());
                hashMap.put("date",currenDate);
                hashMap.put("time",currentTime);

                databaseReference.child("User View").child(RemeberMe.currentOnlineUser.getEmail().replace(".",",")).child("Products")
                        .child(intentProductID).updateChildren(hashMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                databaseReference.child("Admin View").child("abdullah234ktk@gmail,com").child("Products")
                                        .child(intentProductID).updateChildren(hashMap);
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(ProductDetailActivity.this, "Successfully added to cart", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProductDetailActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

    }

    private void loadClickProductInfo() {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products");

        databaseReference.child(intentProductID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productDetail = dataSnapshot.getValue(Products.class);

                Picasso.get().load(productDetail.getImage()).into(detail_product_image);
                product_detail_name.setText(productDetail.getProduct_name());
                product_detail_description.setText(productDetail.getDescription());
                product_detail_price.setText("Price: Rs."+productDetail.getPrice());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
