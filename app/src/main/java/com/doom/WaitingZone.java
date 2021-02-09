package com.doom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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

        getSupportActionBar().hide();

        Sprite circle = new Circle();
        binding.spinKit.setIndeterminateDrawable(circle);

    }
}