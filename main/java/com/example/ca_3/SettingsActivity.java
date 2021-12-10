package com.example.ca_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.Toast;

import com.example.ca_3.databinding.ActivitySettingsBinding;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
ActivitySettingsBinding binding;
FirebaseStorage storage;
FirebaseAuth auth;
FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //hide the toolbar
        getSupportActionBar().hide();
        //create the instance of storage
        storage=FirebaseStorage.getInstance();
        //instance of auth
        auth = FirebaseAuth.getInstance();
        //instance of database
        database=FirebaseDatabase.getInstance();
        //back arrow apply intent
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        //when we set the user name and about and click on save button it would also be updated in firebase
        binding.saveButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String status=binding.etStatus.getText().toString();
              String username=binding.etUserName.getText().toString();
              //key,value ->user and about will be update in firebase also
                HashMap<String,Object> obj=new HashMap<>();
                obj.put("userName",username);
                obj.put("status",status);
                //get the value of status and about in database
                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                        .updateChildren(obj);
                Toast.makeText(SettingsActivity.this,"Profile Updated",Toast.LENGTH_SHORT).show();
            }
        });
        //firebase ->image set(if we go back on setting then updated image will be shown there it would not appear blank
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users=snapshot.getValue(Users.class);
//                        Picasso.get()
//                                .load("https://toppng.com/uploads/preview/icons-logos-emojis-user-icon-png-transparent-11563566676e32kbvynug.png")
//                                .placeholder(R.drawable.ic_avatar)
//                                .into(binding.profileImage);
                        //set status and userName
                        //binding.etStatus.setText(users.getStatus());
                        //binding.etUserName.setText(users.getUserName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        //click on + button then
        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent ,33);
            }
        });
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        //which image is selected by user
        if(data.getData()!=null){
            //path of file
            Uri sFile=data.getData();
            //set the image
            binding.profileImage.setImageURI(sFile);
            //image upload in storage
            final StorageReference reference =storage.getReference().child("profile_picture")
                    .child(FirebaseAuth.getInstance().getUid());
            //image put
            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("profilepic").setValue(uri.toString());
                            Toast.makeText(SettingsActivity.this,"Profile Pic Updated",Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });
        }
    }
}