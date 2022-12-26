package com.example.findhome.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
    private String pri, des, shdes, img, phn, loc, ids, stats;
    private String yes = "yes", no = "no", pending = "review";
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
        stats = getIntent().getStringExtra("status");

        if(stats.equalsIgnoreCase(yes) || stats.equalsIgnoreCase(no)) {
            delete.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
            edit.setEnabled(false);
//            edit.setBackgroundColor(Color.GRAY);
        }else if(stats.equalsIgnoreCase(pending)) {
            delete.setVisibility(View.VISIBLE);
            edit.setEnabled(true);
            edit.setVisibility(View.VISIBLE);
        }

        price.setText("Price: "+pri+" tk");
        location.setText("Location: "+loc);
        description.setText("Description: "+des);
        shortDescription.setText(shdes);
        phoneNo.setText("Phone No:" +phn);
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
                FirebaseDatabase.getInstance().getReference("allImageDatabase").child(ids).removeValue();
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