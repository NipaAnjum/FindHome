package com.example.findhome.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.findhome.R;
import com.example.findhome.adapters.AssetAdapter;
import com.example.findhome.listeners.AssetListener;
import com.example.findhome.model.Item;
import com.example.findhome.pages.AssetDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AssetFragment extends Fragment implements AssetListener {

    private RecyclerView assetRV;
    private TextView noItemMsg;
    private List<Item> assetList;
    private AssetAdapter mAssetAdapter;
    private String currentUser;
    private DatabaseReference ref;
    private FirebaseAuth mFirebaseAuth;
    private int flag = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assetRV = view.findViewById(R.id.your_asset_RV);
        noItemMsg = view.findViewById(R.id.noItemMsg);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        assetList = new ArrayList<>();
        mAssetAdapter = new AssetAdapter(getContext(), assetList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        assetRV.setLayoutManager(linearLayoutManager);
        assetRV.setAdapter(mAssetAdapter);

        FirebaseDatabase.getInstance().getReference().child("images").orderByChild("userId").equalTo(currentUser)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            assetList.add(new Item(
//                                    Objects.requireNonNull(dataSnapshot.child("location").getValue()).toString(),
//                                    Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString(),
//                                    Objects.requireNonNull(dataSnapshot.child("description").getValue()).toString(),
//                                    Objects.requireNonNull(dataSnapshot.child("shortDescription").getValue()).toString(),
//                                    Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString(),
//                                    Objects.requireNonNull(dataSnapshot.child("contactNo").getValue()).toString(),
//                                    Objects.requireNonNull(dataSnapshot.child("itemId").getValue()).toString()
//                            ));
                            assetList.add(dataSnapshot.getValue(Item.class));
                        }
                        if(assetList.size()>0){
                            noItemMsg.setVisibility(View.GONE);
                            assetRV.setVisibility(View.VISIBLE);

                        }
                        else{
                            noItemMsg.setVisibility(View.VISIBLE);
                            assetRV.setVisibility(View.GONE);
                        }
                        mAssetAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void OnAssetPosition(int position) {

        Intent intent = new Intent(getContext(), AssetDetailActivity.class);
        intent.putExtra("contactNo",assetList.get(position).getContactNo());
        intent.putExtra("price",assetList.get(position).getPrice());
        intent.putExtra("location",assetList.get(position).getLocation());
        intent.putExtra("description",assetList.get(position).getDescription());
        intent.putExtra("shortDescription",assetList.get(position).getShortDescription());
        intent.putExtra("image",assetList.get(position).getImage());
        intent.putExtra("itemId",assetList.get(position).getItemId());

        Log.d("++++++++++++", "on asset position " + assetList.get(position).getItemId());
        startActivity(intent);
    }
}