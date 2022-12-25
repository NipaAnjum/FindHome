package com.example.findhome.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.findhome.R;
import com.example.findhome.adapters.AllImageAdapter;
import com.example.findhome.model.AllImage;
import com.example.findhome.model.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ShowAllImageActivity extends AppCompatActivity {

    private String itemIdNo;
    private RecyclerView allImageRV;
    private DatabaseReference ref;

    private ArrayList<AllImage> mArrayList;
    private AllImageAdapter mAllImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_image);

        itemIdNo = getIntent().getStringExtra("itemId");
        allImageRV = findViewById(R.id.allImageRV);

        mArrayList = new ArrayList<>();
        mAllImageAdapter = new AllImageAdapter(this, mArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        allImageRV.setLayoutManager(linearLayoutManager);
        allImageRV.setAdapter(mAllImageAdapter);

        ref = FirebaseDatabase.getInstance().getReference().child("allImageDatabase");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String matcher = dataSnapshot.getKey();
                    if(matcher.equals(itemIdNo)){
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            mArrayList.add(new AllImage(ds.child("link").getValue().toString()));
                        }
                    }
                }
                mAllImageAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}