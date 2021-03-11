package com.doom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.doom.Models.Queries;
import com.doom.databinding.ActivityMoodBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

public class MoodActivity extends AppCompatActivity {

    ActivityMoodBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String profileData;
    ConstraintLayout myLayout;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myLayout = (ConstraintLayout)findViewById(R.id.messageLayout);

        final Queries query = new Queries();
        final DatabaseReference df = database.getReference().child("Queries");
        final String[] userGender = new String[1];
        database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userGender[0] = snapshot.child("gender").getValue().toString();
                binding.profileName.setText(snapshot.child("username").getValue().toString());
                if(snapshot.hasChild("profileImage")) {
                    profileData = snapshot.child("profileImage").getValue().toString();
                    try {
                        final File file = File.createTempFile("image", "jpg");
                        FirebaseStorage.getInstance().getReference().child("images").child(profileData).getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                binding.profileImage.setImageBitmap(bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MoodActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                    binding.profileImage.setImageResource(R.drawable.ic_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.angryMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.setMood("Angry");
                query.setStatus("Available");
                query.setGender(userGender[0]);
                query.setTimeStamp(new Date().getTime());
                //myLayout.setBackgroundColor(Color.parseColor("#EB947F"));
                df.orderByChild("mood").equalTo("Angry").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren())
                        {
                            boolean flag = true;
                            for(DataSnapshot ss: snapshot.getChildren())
                            {
                                Timestamp t1 = new Timestamp(Long.parseLong(ss.child("timeStamp").getValue().toString()));
                                Timestamp t2 = new Timestamp(new Date().getTime());
                                if(((t2.getTime()-t1.getTime())/1000)>=1000) {
                                    FirebaseDatabase.getInstance().getReference().child("Queries").child(ss.getKey()).removeValue();
                                    continue;
                                }
                                String recieverId = ss.getKey();
                                //Toast.makeText(MoodActivity.this, recieverId + " and " + auth.getUid(), Toast.LENGTH_SHORT).show();
                                if(recieverId.equals(auth.getUid())){
                                    Toast.makeText(MoodActivity.this, "Your request has already been registered from another device", Toast.LENGTH_SHORT).show();
                                    continue;
                                }
                                //Toast.makeText(MoodActivity.this, recieverId+"-Akola", Toast.LENGTH_SHORT).show();
                                df.child(recieverId).child("status").setValue("Busy");
                                df.child(recieverId).child("chatterId").setValue(auth.getUid());
                                flag = false;
                                Intent intent = new Intent(MoodActivity.this, ChatBox.class);
                                intent.putExtra("recieverId", recieverId);
                                startActivity(intent);
                                break;
                            }
                            if(flag) {
                                df.child(auth.getUid()).setValue(query);
                                Intent intent = new Intent(MoodActivity.this, WaitingZone.class);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            df.child(auth.getUid()).setValue(query);
                            Intent intent = new Intent(MoodActivity.this, WaitingZone.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        binding.sadMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.setMood("Sad");
                query.setStatus("Available");
                query.setGender(userGender[0]);
                query.setTimeStamp(new Date().getTime());
                df.orderByChild("mood").equalTo("TimePass").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren())
                        {
                            boolean flag = true;
                            for(DataSnapshot ss: snapshot.getChildren())
                            {
                                Timestamp t1 = new Timestamp(Long.parseLong(ss.child("timeStamp").getValue().toString()));
                                Timestamp t2 = new Timestamp(new Date().getTime());
                                if(((t2.getTime()-t1.getTime())/1000)>=1000) {
                                    FirebaseDatabase.getInstance().getReference().child("Queries").child(ss.getKey()).removeValue();
                                    continue;
                                }
                                String recieverId = ss.getKey();
                                if(recieverId.equals(auth.getUid())){
                                    Toast.makeText(MoodActivity.this, "Your request has already been registered from another device", Toast.LENGTH_SHORT).show();
                                    continue;
                                }
                                //Toast.makeText(MoodActivity.this, recieverId+"-Akola", Toast.LENGTH_SHORT).show();
                                flag = false;
                                df.child(recieverId).child("status").setValue("Busy");
                                df.child(recieverId).child("chatterId").setValue(auth.getUid());
                                Intent intent = new Intent(MoodActivity.this, ChatBox.class);
                                intent.putExtra("recieverId", recieverId);
                                startActivity(intent);
                                break;
                            }
                            if(flag) {
                                df.child(auth.getUid()).setValue(query);
                                Intent intent = new Intent(MoodActivity.this, WaitingZone.class);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            df.child(auth.getUid()).setValue(query);
                            Intent intent = new Intent(MoodActivity.this, WaitingZone.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        binding.timePassMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.setMood("TimePass");
                query.setStatus("Available");
                query.setGender(userGender[0]);
                query.setTimeStamp(new Date().getTime());
                df.orderByChild("mood").equalTo("Sad").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren())
                        {
                            boolean flag = true;
                            for(DataSnapshot ss: snapshot.getChildren())
                            {
                                Timestamp t1 = new Timestamp(Long.parseLong(ss.child("timeStamp").getValue().toString()));
                                Timestamp t2 = new Timestamp(new Date().getTime());
                                if(((t2.getTime()-t1.getTime())/1000)>=1000) {
                                    FirebaseDatabase.getInstance().getReference().child("Queries").child(ss.getKey()).removeValue();
                                    continue;
                                }
                                String recieverId = ss.getKey();
                                if(recieverId.equals(auth.getUid())){
                                    Toast.makeText(MoodActivity.this, "Your request has already been registered from another device", Toast.LENGTH_SHORT).show();
                                    continue;
                                }
                                //Toast.makeText(MoodActivity.this, recieverId+"-Akola", Toast.LENGTH_SHORT).show();
                                flag = false;
                                df.child(recieverId).child("status").setValue("Busy");
                                df.child(recieverId).child("chatterId").setValue(auth.getUid());
                                Intent intent = new Intent(MoodActivity.this, ChatBox.class);
                                intent.putExtra("recieverId", recieverId);
                                startActivity(intent);
                                break;
                            }
                            if(flag) {
                                df.orderByChild("mood").equalTo("TimePass").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.hasChildren())
                                        {
                                            boolean flag2 = true;
                                            for(DataSnapshot ss: snapshot.getChildren())
                                            {
                                                Timestamp t1 = new Timestamp(Long.parseLong(ss.child("timeStamp").getValue().toString()));
                                                Timestamp t2 = new Timestamp(new Date().getTime());
                                                if(((t2.getTime()-t1.getTime())/1000)>=1000) {
                                                    FirebaseDatabase.getInstance().getReference().child("Queries").child(ss.getKey()).removeValue();
                                                    continue;
                                                }
                                                String recieverId = ss.getKey();
                                                if(recieverId.equals(auth.getUid())){
                                                    Toast.makeText(MoodActivity.this, "Your request has already been registered from another device", Toast.LENGTH_SHORT).show();
                                                    continue;
                                                }
                                                //Toast.makeText(MoodActivity.this, recieverId+"-Akola", Toast.LENGTH_SHORT).show();
                                                flag2 = false;
                                                df.child(recieverId).child("status").setValue("Busy");
                                                df.child(recieverId).child("chatterId").setValue(auth.getUid());
                                                Intent intent = new Intent(MoodActivity.this, ChatBox.class);
                                                intent.putExtra("recieverId", recieverId);
                                                startActivity(intent);
                                                break;
                                            }
                                            if(flag2){
                                                df.child(auth.getUid()).setValue(query);
                                                Intent intent = new Intent(MoodActivity.this, WaitingZone.class);
                                                startActivity(intent);
                                            }
                                        }
                                        else
                                        {
                                            df.child(auth.getUid()).setValue(query);
                                            Intent intent = new Intent(MoodActivity.this, WaitingZone.class);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                        else
                        {
                            df.orderByChild("mood").equalTo("TimePass").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.hasChildren())
                                    {
                                        boolean flag2 = true;
                                        for(DataSnapshot ss: snapshot.getChildren())
                                        {
                                            Timestamp t1 = new Timestamp(Long.parseLong(ss.child("timeStamp").getValue().toString()));
                                            Timestamp t2 = new Timestamp(new Date().getTime());
                                            if(((t2.getTime()-t1.getTime())/1000)>=1000) {
                                                FirebaseDatabase.getInstance().getReference().child("Queries").child(ss.getKey()).removeValue();
                                                continue;
                                            }
                                            String recieverId = ss.getKey();
                                            if(recieverId.equals(auth.getUid())){
                                                Toast.makeText(MoodActivity.this, "Your request has already been registered from another device", Toast.LENGTH_SHORT).show();
                                                continue;
                                            }
                                            //Toast.makeText(MoodActivity.this, recieverId+"-Akola", Toast.LENGTH_SHORT).show();
                                            flag2 = false;
                                            df.child(recieverId).child("status").setValue("Busy");
                                            df.child(recieverId).child("chatterId").setValue(auth.getUid());
                                            Intent intent = new Intent(MoodActivity.this, ChatBox.class);
                                            intent.putExtra("recieverId", recieverId);
                                            startActivity(intent);
                                            break;
                                        }
                                        if(flag2)
                                        {
                                            df.child(auth.getUid()).setValue(query);
                                            Intent intent = new Intent(MoodActivity.this, WaitingZone.class);
                                            startActivity(intent);
                                        }
                                    }
                                    else
                                    {
                                        df.child(auth.getUid()).setValue(query);
                                        Intent intent = new Intent(MoodActivity.this, WaitingZone.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        binding.passionateMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.setMood("Passionate");
                query.setStatus("Available");
                query.setGender(userGender[0]);
                query.setTimeStamp(new Date().getTime());
                df.orderByChild("mood").equalTo("Passionate").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren())
                        {
                            boolean flag = true;
                            for(DataSnapshot ss: snapshot.getChildren())
                            {
                                Timestamp t1 = new Timestamp(Long.parseLong(ss.child("timeStamp").getValue().toString()));
                                Timestamp t2 = new Timestamp(new Date().getTime());
                                if(((t2.getTime()-t1.getTime())/1000)>=1000) {
                                    FirebaseDatabase.getInstance().getReference().child("Queries").child(ss.getKey()).removeValue();
                                    continue;
                                }
                                //Toast.makeText(MoodActivity.this, userGender[0] + " and " + ss.child("gender").getValue() + " " + ss.getKey(), Toast.LENGTH_SHORT).show();
                                String recieverId = ss.getKey();
                                if(recieverId.equals(auth.getUid())){
                                    Toast.makeText(MoodActivity.this, "Your request has already been registered from another device", Toast.LENGTH_SHORT).show();
                                    continue;
                                }
                                if(userGender[0].equals(ss.child("gender").getValue().toString()))
                                {
                                    //Toast.makeText(MoodActivity.this, "This is working", Toast.LENGTH_SHORT).show();
                                    continue;
                                }
                                //Toast.makeText(MoodActivity.this, recieverId+"-Akola", Toast.LENGTH_SHORT).show();
                                flag = false;
                                df.child(recieverId).child("status").setValue("Busy");
                                df.child(recieverId).child("chatterId").setValue(auth.getUid());
                                Intent intent = new Intent(MoodActivity.this, ChatBox.class);
                                intent.putExtra("recieverId", recieverId);
                                startActivity(intent);
                                break;
                            }
                            if(flag) {
                                df.child(auth.getUid()).setValue(query);
                                Intent intent = new Intent(MoodActivity.this, WaitingZone.class);
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            df.child(auth.getUid()).setValue(query);
                            Intent intent = new Intent(MoodActivity.this, WaitingZone.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.settingsButton:
                Intent intent = new Intent(MoodActivity.this, Settings.class);
                startActivity(intent);
                break;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(MoodActivity.this,"There is no back action, Click again to exit",Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
        return;
    }
}