package com.example.findhome.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.findhome.R;
import com.example.findhome.listeners.AssetListener;
import com.example.findhome.listeners.ItemListener;
import com.example.findhome.model.Item;

import java.util.ArrayList;
import java.util.List;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.ViewHolder> {

    private Context context;
    private List<Item> assetList;
    private AssetListener assetListener;
    private ImageView approveIcon, refuseIcon, pendingIcon;
    private String yes = "yes", no = "no", pending = "review";

    public AssetAdapter(Context context, List<Item> assetList, AssetListener assetListener) {
        this.context = context;
        this.assetList = assetList;
        this.assetListener = assetListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.asset_list,parent,false);
        return new AssetAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.assetLocation.setText(assetList.get(position).getLocation());
        Glide.with(context)
                .load(assetList.get(position).getImage())
                .centerCrop()
                .placeholder(R.drawable.image)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.assetImage.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        if(assetList.get(position).getStatus().equalsIgnoreCase(yes)){
                    approveIcon.setVisibility(View.VISIBLE);
                    pendingIcon.setVisibility(View.GONE);
                    refuseIcon.setVisibility(View.GONE);
        }else if(assetList.get(position).getStatus().equalsIgnoreCase(no)){
                    approveIcon.setVisibility(View.GONE);
                    pendingIcon.setVisibility(View.GONE);
                    refuseIcon.setVisibility(View.VISIBLE);
        }else if(assetList.get(position).getStatus().equalsIgnoreCase(pending)){
                    approveIcon.setVisibility(View.GONE);
                    pendingIcon.setVisibility(View.VISIBLE);
                    refuseIcon.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {return assetList.size();}


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView assetLocation;
        private ImageView assetImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            assetLocation = itemView.findViewById(R.id.assetLocation);
            assetImage = itemView.findViewById(R.id.assetImage);
            approveIcon = itemView.findViewById(R.id.approved);
            refuseIcon = itemView.findViewById(R.id.refused);
            pendingIcon = itemView.findViewById(R.id.pending);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    assetListener.OnAssetPosition(getAdapterPosition());
                }
            });
        }
    }
}