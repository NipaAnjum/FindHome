package com.example.findhome.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.findhome.R;
import com.example.findhome.pages.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UploadFragment extends Fragment {

    private ImageView itemImage;
    private Toolbar uploadToolBar;
    private TextInputEditText location, price, description, shortDescription, contact;
    private AppCompatButton upload_button, select_button;
    private int Pick_Image = 1;
    private int clickCount = 0;
    private Uri uri;
    private String id, userID, assetStatus = "review";
    private FirebaseUser currentUser;
    private DatabaseReference ref, userDB;
    private DatabaseReference reference, root;
    private boolean updatePerformed = false;
    private ArrayList<Uri> imageList;
    private Uri imageUri;
    private int upload_count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemImage = view.findViewById(R.id.give_image);
        location = view.findViewById(R.id.give_location);
        price = view.findViewById(R.id.give_price);
        description = view.findViewById(R.id.give_description);
        shortDescription = view.findViewById(R.id.give_short_description);
        contact = view.findViewById(R.id.give_contactNo);
        select_button = view.findViewById(R.id.select_image_button);
        upload_button = view.findViewById(R.id.make_upload);
        uploadToolBar = view.findViewById(R.id.uploadToolbar);

//        ((AppCompatActivity)getActivity()).setSupportActionBar(uploadToolBar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);

        imageList = new ArrayList<>();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            userID = currentUser.getUid();
        }

        root = FirebaseDatabase.getInstance().getReference().child("images");
        id = root.push().getKey();
//        select_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Selected Picture"), Pick_Image);
//            }
//        });

        select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selected Picture"), Pick_Image);
            }
        });

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location.getText().toString() == null || price.getText().toString() == null || description.getText().toString() == null
                        || shortDescription.getText().toString() == null || contact.getText().toString() == null || uri  == null) {
                    Toast.makeText(getActivity(), "All Information is required", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(clickCount==0) {
                        uploadItem();
                        clickCount += 1;
                    }
                    else Toast.makeText(getContext(), "Wait for the response", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                getActivity().finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Pick_Image && resultCode == RESULT_OK){
            if(data.getClipData() != null){
                int countClipData = data.getClipData().getItemCount();
                int currentImageSelect = 0;
                uri = data.getClipData().getItemAt(currentImageSelect).getUri();
                itemImage.setImageURI(uri);
                itemImage.setVisibility(View.VISIBLE);
                while(currentImageSelect<countClipData){
                    imageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
                    imageList.add(imageUri);
                    currentImageSelect += 1;
                }
            }
        }
    }

private void uploadItem() {

    StorageReference imageFolder = FirebaseStorage.getInstance().getReference();
    Uri oneImage = imageList.get(0);
    StorageReference singleImageName = imageFolder.child("images/" + oneImage.getLastPathSegment());
    singleImageName.putFile(oneImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            singleImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
//                    id = root.push().getKey();
                    Map<String,String> map = new HashMap<>();
                    map.put("location", location.getText().toString());
                    map.put("price", price.getText().toString());
                    map.put("description", description.getText().toString());
                    map.put("shortDescription", shortDescription.getText().toString());
                    map.put("contactNo", contact.getText().toString());
                    map.put("userId", userID);
                    map.put("itemId",id);
                    map.put("image",uri.toString());
                    map.put("status", assetStatus);

                    reference = FirebaseDatabase.getInstance().getReference().child("images").child(id);
                    reference.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startActivity(new Intent(getActivity(), HomeActivity.class));
                            ((Activity) getActivity()).overridePendingTransition(0,0);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed to Upload New Item", Toast.LENGTH_SHORT).show();
                        }
                    });
                    imageList.clear();
                }
            });
        }
    });

    for (upload_count = 0; upload_count < imageList.size(); upload_count++) {
        Uri individualImage = imageList.get(upload_count);
        StorageReference imageName = imageFolder.child("images/" + individualImage.getLastPathSegment());

        imageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = String.valueOf(uri);
                        Map<String, String> hashMap = new HashMap<>();
                        hashMap.put("link", url);

                        reference = FirebaseDatabase.getInstance().getReference().child("allImageDatabase").child(id);
                        reference.push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(getContext(), "Upload Successful", Toast.LENGTH_SHORT).show();
                                Log.d("==============", "============IMAGE DATABASE UPLOAD SUCCESSFUL============ ");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failed to Upload", Toast.LENGTH_SHORT).show();
                            }
                        });
                        imageList.clear();
                    }
                });
            }
        });
    }
    }
}