package com.example.findhome.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.findhome.R;
import com.example.findhome.adapters.AssetAdapter;
import com.example.findhome.adapters.HomeAdapter;
import com.example.findhome.listeners.AssetListener;
import com.example.findhome.model.Item;
import com.example.findhome.pages.AssetDetailActivity;
import com.example.findhome.pages.DetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AssetFragment extends Fragment implements AssetListener {

    private RecyclerView assetRV;
    private List<Item> assetList;
    private AssetAdapter mAssetAdapter;
    private DatabaseReference ref;
    private FirebaseAuth mFirebaseAuth;

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

        assetList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("images")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            assetList.add(new Item(
                                    Objects.requireNonNull(dataSnapshot.child("location").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("description").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("shortDescription").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("contactNo").getValue()).toString()
                            ));
                        }
                        mAssetAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        mAssetAdapter = new AssetAdapter(getContext(), assetList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        assetRV.setLayoutManager(linearLayoutManager);
        assetRV.setAdapter(mAssetAdapter);
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
        startActivity(intent);
    }
}