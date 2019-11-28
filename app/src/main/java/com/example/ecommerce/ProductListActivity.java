package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce.MyClasses.Cart;
import com.example.ecommerce.ViewHolderClasses.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductListActivity extends AppCompatActivity {

    RecyclerView recyclerProductList;
    DatabaseReference databaseReference;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerProductList = findViewById(R.id.recyclerProductList);
        recyclerProductList.setLayoutManager(new LinearLayoutManager(this));
        uid = getIntent().getStringExtra("uid");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin Views").child(uid);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>().setQuery(databaseReference,Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {
                        cartViewHolder.card_product_name.setText(cart.getProduct_name());
                        cartViewHolder.card_product_quantity.setText(cart.getQuantity());
                        cartViewHolder.card_product_price.setText(cart.getPrice());
                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitemlayout,parent,false);
                        return new CartViewHolder(view);
                    }
                };
        recyclerProductList.setAdapter(adapter);
        adapter.startListening();
    }
}
