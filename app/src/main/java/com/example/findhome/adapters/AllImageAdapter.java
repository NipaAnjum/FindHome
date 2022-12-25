package com.example.findhome.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.example.findhome.R;
import com.example.findhome.model.AllImage;

import java.util.ArrayList;

public class AllImageAdapter extends RecyclerView.Adapter<AllImageAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AllImage> imageArrayList;

    public AllImageAdapter(Context context, ArrayList<AllImage> imageArrayList) {
        this.context = context;
        this.imageArrayList = imageArrayList;
    }


    @NonNull
    @Override
    public AllImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllImageAdapter.ViewHolder holder, int position) {

        Log.d("==========", "===========onBindViewHolder: "+ imageArrayList.get(position).getImageUrl());

        Glide.with(context)
                .load(imageArrayList.get(position).getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.image)
                .into(holder.singleImageView);

    }

    @Override
    public int getItemCount() {
        return imageArrayList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        ImageView singleImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            singleImageView = itemView.findViewById(R.id.singleImage);
        }
    }
}
