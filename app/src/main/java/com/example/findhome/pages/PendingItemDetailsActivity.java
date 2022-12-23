package com.example.findhome.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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

public class PendingItemDetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView price, description, shortDescription, phoneNo, location, itemIdNumber;
    private String pri, des, shdes, img, phn, loc, ids, stats;
    private String yes = "yes", no = "no";
    private Button approve, refuse;
    private DatabaseReference refStat;
    private String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_item_details);

        imageView = findViewById(R.id.pendingAimageView);
        price = findViewById(R.id.pendingAprice);
        location = findViewById(R.id.pendingAlocation);
        description = findViewById(R.id.pendingAdescription);
        shortDescription = findViewById(R.id.pendingAshortDesc);
        phoneNo = findViewById(R.id.pendingAphone);
        itemIdNumber = findViewById(R.id.pendingItemIdNo);

        refStat = FirebaseDatabase.getInstance().getReference().child("images");

        approve = findViewById(R.id.approved);
        refuse = findViewById(R.id.refused);

        pri = getIntent().getStringExtra("price");
        loc = getIntent().getStringExtra("location");
        des = getIntent().getStringExtra("description");
        shdes = getIntent().getStringExtra("shortDescription");
        img = getIntent().getStringExtra("image");
        phn = getIntent().getStringExtra("contactNo");
        ids = getIntent().getStringExtra("itemId");
        stats = getIntent().getStringExtra("status");

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

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refStat.child(ids).child("status").setValue(yes);
                approve.setVisibility(View.GONE);
                refuse.setVisibility(View.GONE);
                Toast.makeText(PendingItemDetailsActivity.this, "Selected item is approved!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PendingItemDetailsActivity.this, AdminHomeActivity.class));
            }
        });

        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refStat.child(ids).child("status").setValue(no);
                approve.setVisibility(View.GONE);
                refuse.setVisibility(View.GONE);
                Toast.makeText(PendingItemDetailsActivity.this, "Selected item is refused!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PendingItemDetailsActivity.this, AdminHomeActivity.class));
            }
        });
    }
}