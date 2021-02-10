package com.doom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.doom.Models.Queries;
import com.doom.databinding.ActivityMoodBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MoodActivity extends AppCompatActivity {

    ActivityMoodBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        final Queries query = new Queries();
        final DatabaseReference df = database.getReference().child("Queries");
        final String[] userGender = new String[1];
        database.getReference().child("Users").child(auth.getUid()).child("gender").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userGender[0] = snapshot.getValue().toString();
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
                df.orderByChild("mood").equalTo("Angry").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren())
                        {
                            for(DataSnapshot ss: snapshot.getChildren())
                            {
                                if(ss.getKey() == auth.getUid())
                                    continue;
                                String recieverId = ss.getKey();
                                //Toast.makeText(MoodActivity.this, recieverId+"-Akola", Toast.LENGTH_SHORT).show();
                                df.child(recieverId).child("status").setValue("Busy");
                                df.child(recieverId).child("chatterId").setValue(auth.getUid());
                                Intent intent = new Intent(MoodActivity.this, ChatBox.class);
                                intent.putExtra("recieverId", recieverId);
                                startActivity(intent);
                                break;
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
                df.orderByChild("mood").equalTo("TimePass").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren())
                        {
                            for(DataSnapshot ss: snapshot.getChildren())
                            {
                                if(ss.getKey() == auth.getUid())
                                    continue;
                                String recieverId = ss.getKey();
                                //Toast.makeText(MoodActivity.this, recieverId+"-Akola", Toast.LENGTH_SHORT).show();
                                df.child(recieverId).child("status").setValue("Busy");
                                df.child(recieverId).child("chatterId").setValue(auth.getUid());
                                Intent intent = new Intent(MoodActivity.this, ChatBox.class);
                                intent.putExtra("recieverId", recieverId);
                                startActivity(intent);
                                break;
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
                df.orderByChild("mood").equalTo("Sad").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren())
                        {
                            for(DataSnapshot ss: snapshot.getChildren())
                            {
                                if(ss.getKey() == auth.getUid())
                                    continue;
                                String recieverId = ss.getKey();
                                //Toast.makeText(MoodActivity.this, recieverId+"-Akola", Toast.LENGTH_SHORT).show();
                                df.child(recieverId).child("status").setValue("Busy");
                                df.child(recieverId).child("chatterId").setValue(auth.getUid());
                                Intent intent = new Intent(MoodActivity.this, ChatBox.class);
                                intent.putExtra("recieverId", recieverId);
                                startActivity(intent);
                                break;
                            }
                        }
                        else
                        {
                            df.orderByChild("mood").equalTo("TimePass").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.hasChildren())
                                    {
                                        for(DataSnapshot ss: snapshot.getChildren())
                                        {
                                            if(ss.getKey() == auth.getUid())
                                                continue;
                                            String recieverId = ss.getKey();
                                            //Toast.makeText(MoodActivity.this, recieverId+"-Akola", Toast.LENGTH_SHORT).show();
                                            df.child(recieverId).child("status").setValue("Busy");
                                            df.child(recieverId).child("chatterId").setValue(auth.getUid());
                                            Intent intent = new Intent(MoodActivity.this, ChatBox.class);
                                            intent.putExtra("recieverId", recieverId);
                                            startActivity(intent);
                                            break;
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
                df.orderByChild("mood").equalTo("Passionate").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren())
                        {
                            for(DataSnapshot ss: snapshot.getChildren())
                            {
                                if(ss.getKey() == auth.getUid())
                                    continue;
                                //Toast.makeText(MoodActivity.this, userGender[0] + " and " + ss.child("gender").getValue() + " " + ss.getKey(), Toast.LENGTH_SHORT).show();
                                if(userGender[0].equals(ss.child("gender").getValue().toString()))
                                {
                                    //Toast.makeText(MoodActivity.this, "This is working", Toast.LENGTH_SHORT).show();
                                    continue;
                                }
                                String recieverId = ss.getKey();
                                //Toast.makeText(MoodActivity.this, recieverId+"-Akola", Toast.LENGTH_SHORT).show();
                                df.child(recieverId).child("status").setValue("Busy");
                                df.child(recieverId).child("chatterId").setValue(auth.getUid());
                                Intent intent = new Intent(MoodActivity.this, ChatBox.class);
                                intent.putExtra("recieverId", recieverId);
                                startActivity(intent);
                                break;
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
            case R.id.settings:
                break;
            case R.id.logout:
                break;
        }
        return true;
    }
}