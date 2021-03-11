package com.doom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.doom.databinding.ActivityWaitingZoneBinding;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class WaitingZone extends AppCompatActivity {

    ActivityWaitingZoneBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ConstraintLayout mylayout;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWaitingZoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mylayout = (ConstraintLayout) findViewById(R.id.messageLayout);

        Sprite circle = new Circle();
        binding.spinKit.setIndeterminateDrawable(circle);

        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("Queries").child(auth.getUid()).removeValue();
                Intent intent = new Intent(WaitingZone.this, MoodActivity.class);
                startActivity(intent);
            }
        });

        database.getReference().child("Queries").child(auth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                database.getReference().child("Queries").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String recieverId = snapshot.child("chatterId").getValue().toString();
                        //String myMood = snapshot.child("mood").getValue().toString();
                        database.getReference().child("Queries").child(auth.getUid()).removeValue();
                        database.getReference().child("Queries").child(recieverId).removeValue();
                        /*if(myMood.equals("Angry"))
                            mylayout.setBackgroundColor(Color.parseColor("#EB947F"));
                        else if(myMood.equals("Sad"))
                            mylayout.setBackgroundColor(Color.parseColor("#DEDEDE"));
                        else if(myMood.equals("TimePass"))
                            mylayout.setBackgroundColor(Color.parseColor("#6C9FE0"));
                        else if(myMood.equals("Passionate"))
                            mylayout.setBackgroundColor(Color.parseColor("#E3F35D"));*/

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        String channelId = "my_channel_id";
                        CharSequence channelName = "My Channel";
                        int importance = NotificationManager.IMPORTANCE_DEFAULT;
                        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
                        notificationChannel.enableLights(true);
                        notificationChannel.setLightColor(Color.RED);
                        notificationChannel.enableVibration(false);
                        notificationChannel.setVibrationPattern(new long[]{0L});
                        notificationManager.createNotificationChannel(notificationChannel);

                        int notifyId = 1;

                        Notification notification = new Notification.Builder(WaitingZone.this)
                                .setContentTitle("Pair Found")
                                .setContentText("Your conversation has been setted up!!")
                                .setSmallIcon(R.drawable.ic_chat__1_)
                                .setChannelId(channelId)
                                .setAutoCancel(true)
                                .build();

                        Intent ii = new Intent(WaitingZone.this, ChatBox.class);
                        ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ii.putExtra("recieverId", recieverId);

                        PendingIntent pendingIntent = PendingIntent.getActivity(WaitingZone.this, 0, ii, PendingIntent.FLAG_UPDATE_CURRENT);
                        notification.contentIntent = pendingIntent;

                        notificationManager.notify(notifyId, notification);

                        Intent intent = new Intent(WaitingZone.this, ChatBox.class);
                        intent.putExtra("recieverId", recieverId);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //Toast.makeText(WaitingZone.this, previousChildName + " -> "+ snapshot, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        Toast.makeText(WaitingZone.this,"There is no back action, Click again to exit",Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
        return;
    }
}