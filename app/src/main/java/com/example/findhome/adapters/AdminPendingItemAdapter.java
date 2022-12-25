package com.example.findhome.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.findhome.R;
import com.example.findhome.listeners.AssetListener;
import com.example.findhome.listeners.PendingItemListener;
import com.example.findhome.model.Item;
import com.example.findhome.pages.PendingItemDetailsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminPendingItemAdapter extends RecyclerView.Adapter<AdminPendingItemAdapter.ViewHolder> {

    private Context context;
    private List<Item> assetList;
    private PendingItemListener pendingAssetListener;

    public AdminPendingItemAdapter(Context context, List<Item> assetList, PendingItemListener pendingAssetListener) {
        this.context = context;
        this.assetList = assetList;
        this.pendingAssetListener = pendingAssetListener;
    }

    @NonNull
    @Override
    public AdminPendingItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.pending_items,parent,false);
        return new AdminPendingItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminPendingItemAdapter.ViewHolder holder, int position) {
        holder.pendingItemLocation.setText(assetList.get(position).getLocation());
        holder.pendingItemSD.setText(assetList.get(position).getShortDescription());
        holder.pendingItemId.setText(assetList.get(position).getItemId());


        if (assetList.get(position).getStatus().equals("yes")){
            holder.relativeLayoutBG.setBackgroundResource(R.color.white);
        }else if(assetList.get(position).getStatus().equals("no")){
            holder.relativeLayoutBG.setBackgroundResource(R.color.red);
        }
    }

    @Override
    public int getItemCount() {
        return assetList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView pendingItemLocation,pendingItemSD,pendingItemId;
        private RelativeLayout relativeLayoutBG;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pendingItemLocation = itemView.findViewById(R.id.pendingAssetLocation);
            pendingItemSD = itemView.findViewById(R.id.pendingAssetSD);
            pendingItemId = itemView.findViewById(R.id.pendingAssetId);
            relativeLayoutBG = itemView.findViewById(R.id.pending_asset_relative_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pendingAssetListener.OnPendingAssetPosition(getAdapterPosition());
                }
            });
        }
    }
}
