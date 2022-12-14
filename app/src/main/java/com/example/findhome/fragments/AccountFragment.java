package com.example.findhome.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.findhome.R;
import com.example.findhome.model.User;
import com.example.findhome.pages.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    private CircleImageView userProfile;
    private EditText userName, userEmail;
    private AppCompatButton updateButton;
    private int Pick_Image = 1, clickCount = 0;
    private Uri uri;
    private String id, userImage="";
    private long maxId;
    private DatabaseReference ref;
    private DatabaseReference reference;
    private FirebaseUser currUser;
    private String currUEmail;
    private boolean updatePerformed = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userProfile = view.findViewById(R.id.profile_image);
        userName = view.findViewById(R.id.user_name);
        userEmail = view.findViewById(R.id.user_email);
        updateButton = view.findViewById(R.id.update_button);

        ref = FirebaseDatabase.getInstance().getReference().child("users");

        currUser = FirebaseAuth.getInstance().getCurrentUser();
        currUEmail = currUser.getEmail();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    String e = ds.child("email").getValue().toString();
                    if(e.equals(currUEmail)) {
                        id = ds.getKey();
                        userImage = ds.child("image").getValue().toString();
                        userName.setText(ds.child("name").getValue().toString());
                        userEmail.setText(ds.child("email").getValue().toString());
                        Glide.with(getContext())
                                .load(ds.child("image").getValue().toString())
                                .centerCrop()
                                .placeholder(R.drawable.ic_account)
                                .into(userProfile);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Selected Picture"), Pick_Image);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickCount==0){
                    uploadImage();
                    clickCount += 1;
                }else{
                    Toast.makeText(getContext(), "Wait for the response", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Pick_Image && resultCode == RESULT_OK){
            assert data != null;
            if(data.getData() != null){
                uri = data.getData();
                Log.d("-----------------", " the uri" + uri);
                userProfile.setImageURI(uri);
            }
        }
    }

    private void uploadImage() {
        if(uri != null) {
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/" + UUID.randomUUID().toString());
            ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            updatePerformed = true;
                            Log.d("URL", "onSuccess: " + uri);

                            Map<String, String> map = new HashMap<>();
                            User u = new User();

                            if (userName.getText().toString() == null) {
                                String n = u.getName();
                                map.put("name", n);
                            } else map.put("name", userName.getText().toString());
                            if (userEmail.getText().toString() == null) {
                                String e = u.getEmail();
                                map.put("email", e);
                            } else map.put("email", userEmail.getText().toString());
                            if (uri != null) {
                                map.put("image", uri.toString());
                            }

                            reference = FirebaseDatabase.getInstance().getReference().child("users").child(id);
                            reference.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseAuth.getInstance().getCurrentUser()
                                            .updateEmail(userEmail.getText().toString().trim())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isComplete()) {
                                                        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getActivity(), HomeActivity.class));
                                                        ((Activity) getActivity()).overridePendingTransition(0, 0);
                                                    } else {
                                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getActivity(), HomeActivity.class));
                                                        ((Activity) getActivity()).overridePendingTransition(0, 0);
                                                    }
                                                }
                                            });
                                }
                            });
                        }
                    });
                }
            });
        }
        else{
            Map<String, String> map = new HashMap<>();
            User u = new User();

            if (userName.getText().toString() == null) {
                String n = u.getName();
                map.put("name", n);
            } else map.put("name", userName.getText().toString());
            if (userEmail.getText().toString() == null) {
                String e = u.getEmail();
                map.put("email", e);
            } else map.put("email", userEmail.getText().toString());

//            userImage = u.getImage();
            map.put("image", userImage);

            reference = FirebaseDatabase.getInstance().getReference().child("users").child(id);
            reference.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseAuth.getInstance().getCurrentUser()
                            .updateEmail(userEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()) {
                                        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getActivity(), HomeActivity.class));
                                        ((Activity) getActivity()).overridePendingTransition(0, 0);
                                    } else {
                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getActivity(), HomeActivity.class));
                                        ((Activity) getActivity()).overridePendingTransition(0, 0);
                                    }
                                }
                            });
                }
            });
        }
    }
}