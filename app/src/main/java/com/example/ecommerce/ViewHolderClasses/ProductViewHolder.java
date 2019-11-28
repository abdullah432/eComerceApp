package com.example.ecommerce.ViewHolderClasses;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecommerce.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    public TextView product_name, product_price, product_description;
    public ImageView product_image;


    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        product_name = itemView.findViewById(R.id.product_name);
        product_price = itemView.findViewById(R.id.product_price);
        product_description = itemView.findViewById(R.id.product_description);
        product_image = itemView.findViewById(R.id.product_image);

    }

}
