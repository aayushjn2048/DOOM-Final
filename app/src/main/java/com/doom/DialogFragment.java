package com.doom;

import android.content.Context;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.doom.Adapters.AvatarAdapter;
import com.doom.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    FragmentActivity fragmentActivity;

    public DialogFragment(Settings settings) {
        this.settings = settings;
    }

    public DialogFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        storage = FirebaseStorage.getInstance();
        images = new ArrayList<>();
        fragmentActivity = this.getActivity();

        images.add("cs10000898984263089511.png");
        images.add("cs10000849938093558363.png");
        images.add("cs10000811708184140319.png");
        images.add("cs10000592242416795426.png");
        images.add("cs10000524080030775394.png");
        images.add("cs10000417372154603123.png");
        images.add("cs10000354665834323549.png");
        images.add("cs10000053986926948907.png");
        images.add("cs115015743350173470.png");
        images.add("cs114670826804284719.png");
        images.add("cs114653745181833031.png");
        images.add("cs114572491926134293.png");
        images.add("cs114442194319972023.png");
        images.add("cs114083562476861379.png");
        images.add("cs113835201862675849.png");
        images.add("cs113270871606642660.png");
        images.add("cs113239488699437069.png");
        images.add("cs112858343352712292.png");
        images.add("cs112792777109162058.png");
        images.add("cs112649497221166171.png");
        images.add("cs112531926704693505.png");
        images.add("cs112145447474950623.png");
        images.add("cs111730397949486489.png");
        images.add("cs111464773118169668.png");
        images.add("cs110895378980355805.png");
        images.add("cs110848211297309264.png");
        images.add("cs110790626714649728.png");

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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(fragmentActivity, 2, GridLayoutManager.VERTICAL, false);
        avatarList.setLayoutManager(gridLayoutManager);

        adapter = new AvatarAdapter(images, fragmentActivity, this, settings);
        avatarList.setAdapter(adapter);
        return rootView;
    }
}
