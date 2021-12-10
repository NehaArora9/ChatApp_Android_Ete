package com.example.ca_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ca_3.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
ActivitySignUpBinding binding;
private FirebaseAuth auth;
FirebaseDatabase database;
//for loading when we click signup
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        //Instance Of Firebase
        auth=FirebaseAuth.getInstance();
        //Instance Of Database
        database=FirebaseDatabase.getInstance();
        //For Loading some data
        progressDialog=new ProgressDialog(SignUpActivity.this);
        //set title
        progressDialog.setTitle("Creating Account");
        //set Message
        progressDialog.setMessage("We Are Creating Your Account");
        //Apply OnClickListener On Signup Button
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show progress Dialog
                progressDialog.show();
                //Check Email and Password
                auth.createUserWithEmailAndPassword(binding.etEmail.getText().toString(),binding.etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //close progress dialog
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Users user = new Users(binding.etUserName.getText().toString(),binding.etEmail.getText().toString(),binding.etPassword.getText().toString());
                            //Id of the user who sign in my app
                            String id=task.getResult().getUser().getUid();
                            //Data set of user in Real time Database (child create)
                            database.getReference().child("Users").child(id).setValue(user);
                            Toast.makeText(SignUpActivity.this,"User Created Successfully",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        //for clicking on a textview of already have an account
        binding.tvAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move from signup to signin page
                Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}