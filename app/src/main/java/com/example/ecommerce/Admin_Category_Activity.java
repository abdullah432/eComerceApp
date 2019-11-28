package com.example.ecommerce;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Admin_Category_Activity extends AppCompatActivity {

    public void selectCategory(View view){

        ImageView tappedButton = (ImageView) view;

        String tappedCategory = tappedButton.getTag().toString();

        Intent intent = new Intent(Admin_Category_Activity.this,Admin_AddNewProduct_Activity.class);
        intent.putExtra("category",tappedCategory);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_category_activity);
    }

    public void LogOut(View view){
        Intent intent = new Intent(Admin_Category_Activity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void checkNewOrder(View view){
        Intent intent = new Intent(Admin_Category_Activity.this,adminNewOrderActivity.class);
        startActivity(intent);
    }
}
