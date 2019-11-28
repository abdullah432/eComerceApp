package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okio.InflaterSource;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce.MyClasses.Orders;
import com.example.ecommerce.ViewHolderClasses.AdminOrderView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.zip.Inflater;

public class adminNewOrderActivity extends AppCompatActivity {

    private RecyclerView orderRecyclerView;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Orders> options =
                new FirebaseRecyclerOptions.Builder<Orders>().setQuery(databaseReference,Orders.class).build();

        FirebaseRecyclerAdapter<Orders, AdminOrderView> adapter = new FirebaseRecyclerAdapter<Orders, AdminOrderView>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrderView adminOrderView, int i, @NonNull final Orders orders) {
                adminOrderView.userNameAdminView.setText(orders.getName());
                adminOrderView.phoneAdminView.setText(orders.getContact());
                adminOrderView.addressAdminView.setText(orders.getAddress());
                adminOrderView.dateAdminView.setText(orders.getDate());

                adminOrderView.view_all_products.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(adminNewOrderActivity.this,ProductListActivity.class);
                        intent.putExtra("uid",orders.getUserID());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public AdminOrderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.neworderview,parent,false);
                return new AdminOrderView(view);
            }
        };

        orderRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
