package com.example.ecommerce.ViewHolderClasses;

import android.view.View;
import android.widget.TextView;

import com.example.ecommerce.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartViewHolder extends RecyclerView.ViewHolder {

    public TextView card_product_name, card_product_quantity, card_product_price;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        card_product_name = itemView.findViewById(R.id.card_product_name);
        card_product_quantity = itemView.findViewById(R.id.card_product_quantity);
        card_product_price = itemView.findViewById(R.id.card_product_price);
    }

}
