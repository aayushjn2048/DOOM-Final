package com.doom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.doom.Adapters.ChatAdapter;
import com.doom.Models.Message;
import com.doom.databinding.ActivityChatBoxBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatBox extends AppCompatActivity {

    ActivityChatBoxBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBoxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderId = auth.getUid();
        final String recieverId = getIntent().getStringExtra("recieverId");
        /*String username = getIntent().getStringExtra("username");
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.profileName.setText(username);
        Picasso.get().load(profilePic).placeholder(R.drawable.ic_profile).into(binding.profileImage);*/

        final ArrayList<Message> msgList = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(msgList, this, recieverId);
        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        final String senderRoom = senderId + recieverId;
        final String recieverRoom = recieverId + senderId;

        database.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msgList.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren())
                {
                    Message model = snapshot1.getValue(Message.class);
                    model.setMessageId(snapshot1.getKey());
                    msgList.add(model);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = binding.editMessage.getText().toString();
                final Message model = new Message(senderId, msg);


                model.setTimestamp(new Date().getTime());
                binding.editMessage.setText("");

                database.getReference().child("chats").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("chats").child(recieverRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("chats").child(senderRoom).removeValue();
                database.getReference().child("chats").child(recieverRoom).removeValue();
                Intent intent = new Intent(ChatBox.this, MoodActivity.class);
                startActivity(intent);
            }
        });
    }
}