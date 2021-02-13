package com.doom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.doom.Models.Users;
import com.doom.databinding.ActivityRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    RadioGroup radioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(RegistrationActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");

        binding.tvreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.etEmail.getText().toString().isEmpty()) {
                    binding.etEmail.setError("This field cannot remain empty");
                    return;
                }
                if (binding.etPassword.getText().toString().isEmpty()) {
                    binding.etPassword.setError("This field cannot remain empty");
                    return;
                }
                if (binding.etuserName.getText().toString().isEmpty()) {
                    binding.etuserName.setError("This field cannot remain empty");
                    return;
                }
                radioGroup = (RadioGroup)findViewById(R.id.gender);
                if(radioGroup.getCheckedRadioButtonId()==-1) {
                    binding.female.setError("This field cannot remain empty");
                    return;
                }
                radioButton = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                final String genderVal = radioButton.getText().toString();
                progressDialog.show();

                auth.createUserWithEmailAndPassword
                        (binding.etEmail.getText().toString(), binding.etPassword.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    final String id = task.getResult().getUser().getUid();
                                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Users user = new Users(binding.etuserName.getText().toString(), binding.etEmail.getText().toString(),genderVal);

                                                database.getReference().child("Users").child(id).setValue(user);
                                                Toast.makeText(RegistrationActivity.this, "Registered Successfully, Please check your email for verification", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });

                                } else {
                                    Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }

                            }
                        });

            }
        });

    }
    @Override
    public void onBackPressed() {
        Toast.makeText(RegistrationActivity.this,"There is no back action",Toast.LENGTH_SHORT).show();
        return;
    }
}