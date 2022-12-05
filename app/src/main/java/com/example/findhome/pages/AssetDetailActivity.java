package com.example.findhome.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.findhome.R;

public class AssetDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView price, description, shortDescription, phoneNo, location;
    String pri, des, shdes, img, phn, loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_detail);

        imageView = findViewById(R.id.aimageView);
        price = findViewById(R.id.aprice);
        location = findViewById(R.id.alocation);
        description = findViewById(R.id.adescription);
        shortDescription = findViewById(R.id.ashortDesc);
        phoneNo = findViewById(R.id.aphone);

        pri = getIntent().getStringExtra("price");
        loc = getIntent().getStringExtra("location");
        des = getIntent().getStringExtra("description");
        shdes = getIntent().getStringExtra("shortDescription");
        img = getIntent().getStringExtra("image");
        phn = getIntent().getStringExtra("contactNo");

        price.setText(pri+" tk");
        location.setText(loc);
        description.setText(des);
        shortDescription.setText(shdes);
        phoneNo.setText(phn);
        Glide.with(this)
                .load(img)
                .centerCrop()
                .placeholder(R.drawable.image)
                .into(imageView);
    }
}