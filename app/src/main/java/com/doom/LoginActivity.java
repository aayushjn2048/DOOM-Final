package com.doom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.doom.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    ActivityLoginBinding binding;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        if (auth.getCurrentUser() != null && auth.getCurrentUser().isEmailVerified()) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            Intent intent = new Intent(LoginActivity.this, MoodActivity.class);
            startActivity(intent);
            finish();
            // or do some other stuff that you want to do
        }

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Signing In");
        progressDialog.setMessage("Signing you in...");

        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.etEmail.getText().toString().isEmpty())
                {
                    binding.etEmail.setError("This field cannot remain empty");
                    return;
                }
                if(binding.etPassword.getText().toString().isEmpty())
                {
                    binding.etPassword.setError("This field cannot remain empty");
                    return;
                }
                progressDialog.show();
                auth.signInWithEmailAndPassword(binding.etEmail.getText().toString(),binding.etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //Toast.makeText(LoginActivity.this, "Signing In working!!", Toast.LENGTH_SHORT).show();
                        if(task.isSuccessful())
                        {
                            if(auth.getCurrentUser().isEmailVerified()) {
                                //Toast.makeText(LoginActivity.this, "Verification Completed!!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginActivity.this, MoodActivity.class);
                                startActivity(i);
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,"Please verify your email",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        binding.tvreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });

        binding.tvforgotpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordresetdialog=new AlertDialog.Builder(view.getContext(),R.style.DialogTheme);
                passwordresetdialog.setTitle("RESET PASSWORD");
                passwordresetdialog.setMessage("Enter your email to receive reset link");
                passwordresetdialog.setView(resetMail);

                passwordresetdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final String mailId=resetMail.getText().toString();

                        auth.sendPasswordResetEmail(mailId).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this,"Reset link sent to "+ mailId,Toast.LENGTH_LONG).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this,"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                passwordresetdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                passwordresetdialog.create().show();
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
        Toast.makeText(LoginActivity.this,"There is no back action, Click again to exit",Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
        return;
    }
}