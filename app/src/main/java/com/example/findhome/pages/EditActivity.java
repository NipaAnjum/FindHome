package com.example.findhome.pages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.findhome.R;
import com.example.findhome.model.Item;
import com.example.findhome.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditActivity extends AppCompatActivity {

    private ImageView assetImage;
    private EditText prices, locations, descriptions,shortDescriptions, phoneNumbers;
    private AppCompatButton updateAssetButton;
    private int Pick_Image = 1, clickCount = 0;
    private Uri uri;
    private String id, assetId;
    private final String stats = "review";
    private String userID, imageOfTheAsset;
    private FirebaseUser currentUser;
    private long maxId;
    private DatabaseReference ref;
    private DatabaseReference reference;
    private boolean updatePerformed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        assetImage = findViewById(R.id.edit_asset_image);
        prices = findViewById(R.id.edit_price);
        locations = findViewById(R.id.edit_location);
        descriptions = findViewById(R.id.edit_description);
        shortDescriptions = findViewById(R.id.edit_short_description);
        phoneNumbers = findViewById(R.id.edit_phone_no);
        updateAssetButton = findViewById(R.id.update_asset_button);

        assetId = getIntent().getStringExtra("itemID");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            userID = currentUser.getUid();
        }

        ref = FirebaseDatabase.getInstance().getReference().child("images");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Item item = snapshot.getValue(Item.class);
                if(item != null){
                    imageOfTheAsset = item.getImage();
                    prices.setText(item.getPrice());
                    locations.setText(item.getLocation());
                    descriptions.setText(item.getDescription());
                    shortDescriptions.setText(item.getShortDescription());
                    phoneNumbers.setText(item.getContactNo());
                    id =  snapshot.getKey();
                    Glide.with(getApplicationContext())
                            .load(item.getImage())
                            .centerCrop()
                            .placeholder(R.drawable.image)
                            .into(assetImage);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        assetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selected Picture"), Pick_Image);
            }
        });

        updateAssetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickCount==0){
                    updateAll();
                    clickCount += 1;
                }else{
                    Toast.makeText(EditActivity.this, "Wait for the response", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Pick_Image && resultCode == RESULT_OK){
            assert data != null;
            if(data.getData() != null){
                uri = data.getData();
                assetImage.setImageURI(uri);
            }
        }
    }

    private void updateAll() {
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/"+ UUID.randomUUID().toString());
        if(uri != null) {
            ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            updatePerformed = true;
                            Log.d("URL", "onSuccess: " + uri);

                            Map<String, String> map = new HashMap<>();
                            Item i = new Item();
                            if(prices.getText().toString() != null) map.put("price", prices.getText().toString());
                            else{
                                String prc = i.getPrice();
                                map.put("price", prc);
                            }
                            if(locations.getText().toString() != null) map.put("location", locations.getText().toString());
                            else{
                                String loc = i.getLocation();
                                map.put("location", loc);
                            }
                            if(shortDescriptions.getText().toString() != null) map.put("shortDescription", shortDescriptions.getText().toString());
                            else{
                                String sd = i.getShortDescription();
                                map.put("shortDescription", sd);
                            }
                            if(descriptions.getText().toString() != null) map.put("description", descriptions.getText().toString());
                            else{
                                String des = i.getDescription();
                                map.put("description", des);
                            }
                            if(phoneNumbers.getText().toString() != null) map.put("contactNo", phoneNumbers.getText().toString());
                            else{
                                String prc = i.getContactNo();
                                map.put("contactNo", prc);
                            }
                            if(assetId != null) map.put("itemId", assetId);
                            else{
                                String asId = i.getItemId();
                                map.put("itemId", asId);
                            }
                            if(userID != null) map.put("userId", userID);
                            else{
                                String uid = i.getUserId();
                                map.put("userId", uid);
                            }
                            map.put("image", uri.toString());
                            map.put("status", stats);

                            reference = FirebaseDatabase.getInstance().getReference().child("images").child(id);
                            reference.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(EditActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditActivity.this, HomeActivity.class));
                                }
                            });
                        }
                    });
                }
            });
        }
        else{
            Map<String, String> map = new HashMap<>();
            Item i = new Item();
            if(prices.getText().toString() != null) map.put("price", prices.getText().toString());
            else{
                String prc = i.getPrice();
                map.put("price", prc);
            }
            if(locations.getText().toString() != null) map.put("location", locations.getText().toString());
            else{
                String loc = i.getLocation();
                map.put("location", loc);
            }
            if(shortDescriptions.getText().toString() != null) map.put("shortDescription", shortDescriptions.getText().toString());
            else{
                String sd = i.getShortDescription();
                map.put("shortDescription", sd);
            }
            if(descriptions.getText().toString() != null) map.put("description", descriptions.getText().toString());
            else{
                String des = i.getDescription();
                map.put("description", des);
            }
            if(phoneNumbers.getText().toString() != null) map.put("contactNo", phoneNumbers.getText().toString());
            else{
                String prc = i.getContactNo();
                map.put("contactNo", prc);
            }
            if(assetId != null) map.put("itemId", assetId);
            else{
                String asId = i.getItemId();
                map.put("itemId", asId);
            }
            if(userID != null) map.put("userId", userID);
            else{
                String uid = i.getUserId();
                map.put("userId", uid);
            }
            map.put("image", imageOfTheAsset);
            map.put("status", stats);
            reference = FirebaseDatabase.getInstance().getReference().child("images").child(id);
            reference.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(EditActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditActivity.this, HomeActivity.class));
                }
            });
        }
    }
}