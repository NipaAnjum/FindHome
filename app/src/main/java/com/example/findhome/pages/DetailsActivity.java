package com.example.findhome.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;import com.bumptech.glide.Glide;
import com.example.findhome.R;


public class DetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView price, description, shortDescription, phoneNo;
    private Button makeCall;
    String pri, des, shdes, img, phn;

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

        pri = getIntent().getStringExtra("price");
        des = getIntent().getStringExtra("description");
        shdes = getIntent().getStringExtra("shortDescription");
        img = getIntent().getStringExtra("image");
        phn = getIntent().getStringExtra("contactNo");

//        Log.d(img, "Image");
        
        price.setText(pri+"tk");
        description.setText(des);
        shortDescription.setText(shdes);
        phoneNo.setText(phn);
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
    }
}