package com.example.ca_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ca_3.databinding.ActivitySignInBinding;
import com.example.ca_3.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {
    //For BackEnd
    ActivitySignInBinding binding;
    //For loading Data After Clicking
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    //Google SignIn
    GoogleSignInClient mGoogleSignInClient;
    //for database
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Remove Toolbar From  The Very Begining Of Mainactivity
        getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();


        //instance of database
        database=FirebaseDatabase.getInstance();
        //For Loading some Data
        progressDialog=new ProgressDialog(SignInActivity.this);
        //set title
        progressDialog.setTitle("Login");
        //set Message
        progressDialog.setMessage("Login to your account");

        //Configure google signin
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //to remove this error build->rebuld
               // .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient=GoogleSignIn.getClient(this,gso);


//if email and password is empty then it shows an error message
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.etEmail.getText().toString().isEmpty()){
                    binding.etEmail.setError("Please enter your email ");

                }
                if(binding.etPassword.getText().toString().isEmpty()){
                    binding.etPassword.setError("Please enter your password ");
                    return;

                }
                progressDialog.show();
                auth.signInWithEmailAndPassword(binding.etEmail.getText().toString(),binding.etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Intent intent=new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(intent);

                        }
                        else
                        {
                            Toast.makeText(SignInActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });
        //for clicking on a textview for signup
        binding.tvclickSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //goes from signin to signup page
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        //when google button is clicked then signin methods calls
        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        //if user sign in then again it will not ask from the user
        // to sign in again and again

        if(auth.getCurrentUser()!=null)
        {
            Intent intent=new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
    //below 100 take any code
    int RC_SIGN_IN=65;
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }
// method
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            //create user,set its id,name,pic
                            Users users=new Users();
                            users.setUserId(user.getUid());
                            users.setUserName(user.getDisplayName());
                            users.setProfilepic(user.getPhotoUrl().toString());
                            //use database
                            database.getReference().child("Users").child(user.getUid()).setValue(users);

                            Intent intent=new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(SignInActivity.this,"Sign In With Google",Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            Snackbar.make(binding.getRoot(),"Authentication Failed",Snackbar.LENGTH_SHORT).show();
                           // updateUI(null);
                        }
                    }
                });
    }
}