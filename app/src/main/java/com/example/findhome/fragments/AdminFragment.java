package com.example.findhome.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.findhome.R;
import com.example.findhome.adapters.AdminPendingItemAdapter;
import com.example.findhome.adapters.AssetAdapter;
import com.example.findhome.listeners.AssetListener;
import com.example.findhome.listeners.PendingItemListener;
import com.example.findhome.model.Item;
import com.example.findhome.pages.AdminHomeActivity;
import com.example.findhome.pages.AssetDetailActivity;
import com.example.findhome.pages.LoginActivity;
import com.example.findhome.pages.PendingItemDetailsActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AdminFragment extends Fragment implements PendingItemListener {

    private ExtendedFloatingActionButton logOut;
    private RecyclerView pendingListView;
    private FirebaseAuth mFirebaseAuth;
    private AdminPendingItemAdapter mPendingItemAdapter;
    private List<Item> pendingList;
    private Item mItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        logOut = view.findViewById(R.id.logOutAdmin);
        pendingListView = view.findViewById(R.id.pendingList);

        pendingList = new ArrayList<>();
        mItem = new Item();

        mPendingItemAdapter = new AdminPendingItemAdapter(getContext(), pendingList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        pendingListView.setLayoutManager(linearLayoutManager);
        pendingListView.setAdapter(mPendingItemAdapter);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("images")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            pendingList.add(0,dataSnapshot.getValue(Item.class));
                        }
                        mPendingItemAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void OnPendingAssetPosition(int position) {

        Intent intent = new Intent(getContext(), PendingItemDetailsActivity.class);
        intent.putExtra("contactNo",pendingList.get(position).getContactNo());
        intent.putExtra("price",pendingList.get(position).getPrice());
        intent.putExtra("location",pendingList.get(position).getLocation());
        intent.putExtra("description",pendingList.get(position).getDescription());
        intent.putExtra("shortDescription",pendingList.get(position).getShortDescription());
        intent.putExtra("image",pendingList.get(position).getImage());
        intent.putExtra("itemId",pendingList.get(position).getItemId());
        intent.putExtra("status",pendingList.get(position).getStatus());
        startActivity(intent);
    }
}