package com.example.ecommerce.ViewHolderClasses;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerce.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminOrderView extends RecyclerView.ViewHolder {

    public TextView userNameAdminView;
    public TextView phoneAdminView;
    public TextView addressAdminView;
    public TextView dateAdminView;
    public Button view_all_products;

    public AdminOrderView(@NonNull View itemView) {
        super(itemView);

        userNameAdminView = itemView.findViewById(R.id.userNameAdminView);
        phoneAdminView = itemView.findViewById(R.id.phoneAdminView);
        addressAdminView = itemView.findViewById(R.id.addressAdminView);
        dateAdminView = itemView.findViewById(R.id.dateAdminView);
        view_all_products = itemView.findViewById(R.id.view_all_products);
    }
}
