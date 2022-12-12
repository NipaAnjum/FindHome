package com.example.findhome.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.findhome.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AssetDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView price, description, shortDescription, phoneNo, location, itemIdNumber;
    String pri, des, shdes, img, phn, loc, ids;
    private Button edit, delete;
    private DatabaseReference ref;
    private String itemId;

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
        itemIdNumber = findViewById(R.id.itemIdNo);

        edit = findViewById(R.id.editButton);
        delete = findViewById(R.id.deleteButton);

        pri = getIntent().getStringExtra("price");
        loc = getIntent().getStringExtra("location");
        des = getIntent().getStringExtra("description");
        shdes = getIntent().getStringExtra("shortDescription");
        img = getIntent().getStringExtra("image");
        phn = getIntent().getStringExtra("contactNo");
        ids = getIntent().getStringExtra("itemId");

        price.setText(pri+" tk");
        location.setText(loc);
        description.setText(des);
        shortDescription.setText(shdes);
        phoneNo.setText(phn);
        itemIdNumber.setText(ids);
        Glide.with(this)
                .load(img)
                .centerCrop()
                .placeholder(R.drawable.image)
                .into(imageView);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("images").child(ids).removeValue();
                Toast.makeText(AssetDetailActivity.this, "Selected item is deleted!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AssetDetailActivity.this, HomeActivity.class));
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssetDetailActivity.this, EditActivity.class);
                intent.putExtra("itemID", ids);
                startActivity(intent);
            }
        });
    }
}