package com.doom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.doom.Adapters.AvatarAdapter;
import com.doom.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    FirebaseStorage storage;
    RecyclerView avatarList;
    List<String> images;
    AvatarAdapter adapter;
    Settings settings;

    public DialogFragment(Settings settings) {
        this.settings = settings;
    }

    public DialogFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        storage = FirebaseStorage.getInstance();
        images = new ArrayList<>();

        FirebaseStorage.getInstance().getReference().child("images").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    images.add(item.getName());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("storageException", e.getMessage());
            }
        });

        View rootView = inflater.inflate(R.layout.avatar_palette, container);
        avatarList = (RecyclerView) rootView.findViewById(R.id.avatarList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getActivity(), 2, GridLayoutManager.VERTICAL, false);
        avatarList.setLayoutManager(gridLayoutManager);

        adapter = new AvatarAdapter(images, this.getActivity(), this, settings);
        avatarList.setAdapter(adapter);
        return rootView;
    }
}
