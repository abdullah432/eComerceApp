package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.MyClasses.Cart;
import com.example.ecommerce.RememberMe.RemeberMe;
import com.example.ecommerce.ViewHolderClasses.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private TextView cartTotalPriceTxt;
    private RecyclerView recylerView;

    private DatabaseReference databaseReference;

    private int overAllPriceOfProducts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartTotalPriceTxt = findViewById(R.id.cartTotalPriceTxt);
        recylerView = findViewById(R.id.recylerView);
        recylerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart List");
    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.
                Builder<Cart>().setQuery(databaseReference.child("User View").child(RemeberMe.currentOnlineUser.getEmail().replace(".",","))
                .child("Products"),Cart.class).build();

        final FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, final int i, @NonNull final Cart cart) {
                cartViewHolder.card_product_name.setText(cart.getProduct_name());
                cartViewHolder.card_product_quantity.setText("Quantity = "+cart.getQuantity());
                cartViewHolder.card_product_price.setText("PRICE: Rs."+cart.getPrice());

                final int TotalPriceOfOneProduct = (Integer.valueOf(cart.getPrice())) * (Integer.valueOf(cart.getQuantity()));
                overAllPriceOfProducts += TotalPriceOfOneProduct;

                cartTotalPriceTxt.setText("Total Price: Rs."+String.valueOf(overAllPriceOfProducts));

                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence option[] = new CharSequence[]
                                {
                                        "Edit","Delete"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){
                                    Intent intent = new Intent(CartActivity.this,ProductDetailActivity.class);
                                    intent.putExtra("pid",cart.getPid());
                                    startActivity(intent);
                                }
                                else if(which == 1){
                                    //remove from admin view
                                    databaseReference.child("Admin View")
                                            .child("abdullah234ktk@gmail,com").child("Products").child(cart.getPid()).removeValue();

                                    //remove from user view
                                    databaseReference.child("User View")
                                            .child(RemeberMe.currentOnlineUser.getEmail().replace(".",","))
                                            .child("Products").child(cart.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                overAllPriceOfProducts -= TotalPriceOfOneProduct;
                                                cartTotalPriceTxt.setText("Total Price: Rs."+String.valueOf(overAllPriceOfProducts));
                                                Toast.makeText(CartActivity.this,"Product is removed Successfully",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitemlayout, parent, false);
                return new CartViewHolder(view);
            }
        };

        recylerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        overAllPriceOfProducts = 0;
    }

    public void nextButton(View view){
        databaseReference = databaseReference.child("User View").child(RemeberMe.currentOnlineUser.getEmail().replace(".",","));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Intent intent = new Intent(CartActivity.this,ConfirmActivity.class);
                    intent.putExtra("totalPrice",String.valueOf(overAllPriceOfProducts));
                    startActivity(intent);
                }
                else {
                    Toast.makeText(CartActivity.this,"Cart is empty",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
