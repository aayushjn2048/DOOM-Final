package com.doom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.doom.databinding.ActivityWaitingZoneBinding;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.DoubleBounce;

public class WaitingZone extends AppCompatActivity {

    ActivityWaitingZoneBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWaitingZoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Sprite circle = new Circle();
        binding.spinKit.setIndeterminateDrawable(circle);

        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WaitingZone.this, MoodActivity.class);
                startActivity(intent);
            }
        });

    }
}