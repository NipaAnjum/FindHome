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
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
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
    private LottieAnimationView emptyView;
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
        emptyView = view.findViewById(R.id.emptyAnimation);

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
//                            Log.d("================", "Asset fragment "+ dataSnapshot);
                            assetList.add(0,dataSnapshot.getValue(Item.class));
                        }
                        if(assetList.size()>0){
                            emptyView.setVisibility(View.GONE);
                            noItemMsg.setVisibility(View.GONE);
                            assetRV.setVisibility(View.VISIBLE);

                        }
                        else{
                            emptyView.setVisibility(View.VISIBLE  );
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
        intent.putExtra("status",assetList.get(position).getStatus());

//        Log.d("++++++++++++", "on asset position " + assetList.get(position).getItemId());
        startActivity(intent);
    }
}