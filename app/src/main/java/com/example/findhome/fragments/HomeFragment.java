package com.example.findhome.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.findhome.R;
import com.example.findhome.adapters.HomeAdapter;
import com.example.findhome.listeners.ItemListener;
import com.example.findhome.model.Item;
import com.example.findhome.pages.DetailsActivity;
import com.example.findhome.pages.LoginActivity;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment implements ItemListener {

    private RecyclerView topDealRV;
    private EditText searchBar;
    private HomeAdapter adapter;
    private List<Item> itemList;
    private List<Item> filterList;
    private CircleImageView profileImage;
    private TextView username;
    private final int searchFlag = 0;
    private DatabaseReference ref;
    private FirebaseUser currUser;
    private String currUEmail;
    private final String yes = "yes";
    private FirebaseAuth mFirebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // LOGOUT POPUP
        profileImage = v.findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder logoutAlert = new AlertDialog.Builder(getActivity());
                logoutAlert.setTitle("Logout")
                        .setMessage("Do you want to logout?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mFirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                ((Activity) getActivity()).overridePendingTransition(0, 0);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();


            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        topDealRV = view.findViewById(R.id.top_deal_RV);
        profileImage = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.user_name_show);
        searchBar = view.findViewById(R.id.search);


        ref = FirebaseDatabase.getInstance().getReference().child("users");
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        currUEmail = currUser.getEmail();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    String e = ds.child("email").getValue().toString();
                    if(e.equals(currUEmail)){
                        username.setText("Welcome "+ds.child("name").getValue().toString());
                        if(getContext()!=null)
                        Glide.with(getContext())
                            .load(ds.child("image").getValue())
                            .centerCrop()
                            .placeholder(R.drawable.ic_account)
                            .into(profileImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        itemList = new ArrayList<>();
        filterList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("images")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.child("status").getValue().toString().equalsIgnoreCase(yes)){

                                itemList.add(0,new Item(
                                        Objects.requireNonNull(dataSnapshot.child("location").getValue()).toString(),
                                        Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString(),
                                        Objects.requireNonNull(dataSnapshot.child("description").getValue()).toString(),
                                        Objects.requireNonNull(dataSnapshot.child("shortDescription").getValue()).toString(),
                                        Objects.requireNonNull(dataSnapshot.child("image").getValue()).toString(),
                                        Objects.requireNonNull(dataSnapshot.child("contactNo").getValue()).toString(),
                                        Objects.requireNonNull(dataSnapshot.child("status").getValue()).toString()
                                ));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        adapter = new HomeAdapter(getContext(), itemList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        topDealRV.setLayoutManager(linearLayoutManager);
        topDealRV.setAdapter(adapter);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2, GridLayoutManager.HORIZONTAL, false);
//        topDealRV.setLayoutManager(gridLayoutManager);
//        topDealRV.setHasFixedSize(true);
//        topDealRV.setAdapter(adapter);

        //SEARCH
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filterList.clear();

                if(s.toString().isEmpty()){
                    noFilter();
                }
                else{
                    filter(s.toString());
                }
            }
        });
    }

    private void noFilter() {
        adapter = new HomeAdapter(getContext(), itemList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        topDealRV.setLayoutManager(linearLayoutManager);
        topDealRV.setAdapter(adapter);
    }

    private void filter(String text) {
        for( Item item:itemList){
            if(item.getLocation().toLowerCase().contains(text.toLowerCase())){
                filterList.add(item);
            }
        }
        adapter = new HomeAdapter(getContext(), filterList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        topDealRV.setLayoutManager(linearLayoutManager);
        topDealRV.setAdapter(adapter);
    }

    @Override
    public void OnItemPosition(int position) {

        Intent intent = new Intent(getContext(),DetailsActivity.class);
        intent.putExtra("contactNo",itemList.get(position).getContactNo());
        intent.putExtra("price",itemList.get(position).getPrice());
        intent.putExtra("location",itemList.get(position).getLocation());
        intent.putExtra("description",itemList.get(position).getDescription());
        intent.putExtra("shortDescription",itemList.get(position).getShortDescription());
        intent.putExtra("image",itemList.get(position).getImage());
        startActivity(intent);
    }
}