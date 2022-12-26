package com.example.findhome.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;import com.bumptech.glide.Glide;
import com.example.findhome.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class DetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView price, description, shortDescription, phoneNo, location;
    private FloatingActionButton more;
    private FloatingActionButton makeCall;
    private FloatingActionButton map;
    String pri, des, shdes, img, phn, itemId, loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imageView = findViewById(R.id.imageView);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        shortDescription = findViewById(R.id.short_description);
        makeCall = findViewById(R.id.make_call);
        phoneNo = findViewById(R.id.phone_no);
        location = findViewById(R.id.location);
        more = findViewById(R.id.moreImages);
        map = findViewById(R.id.goMap);

        pri = getIntent().getStringExtra("price");
        des = getIntent().getStringExtra("description");
        shdes = getIntent().getStringExtra("shortDescription");
        img = getIntent().getStringExtra("image");
        phn = getIntent().getStringExtra("contactNo");
        itemId = getIntent().getStringExtra("itemId");
        loc = getIntent().getStringExtra("location");

//        Log.d("==================", "item id ============ " + itemId);
        
        price.setText("Price: "+pri+"tk");
        description.setText(des);
        shortDescription.setText(shdes);
        location.setText("Address: "+loc);
        phoneNo.setText("Phone No: "+phn);
        Glide.with(this)
                .load(img)
                .centerCrop()
                .placeholder(R.drawable.ic_account)
                .into(imageView);


        makeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phn));
                startActivity(intent);
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, ShowAllImageActivity.class);
                intent.putExtra("itemId", itemId);
                startActivity(intent);
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "geo:0, 0?q=" + loc;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });
    }
}