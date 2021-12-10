package com.example.ca_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ca_3.Adapters.FragmentsAdapter;
import com.example.ca_3.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef =database.getReference("chats");
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String value = snapshot.child("message").getValue().toString();
//                Log.d("tag","==-->"+value.toString());
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(MainActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
//            }
//        });

        //myRef.setValue("Hello World");
        auth=FirebaseAuth.getInstance();
        //for tab layout
        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
         binding.tablayout.setupWithViewPager(binding.viewPager);
    }
    //for creating option
    public boolean onCreateOptionsMenu(Menu menu){
       //use menu inflater
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //when our item is selected
    public boolean onOptionsItemSelected(MenuItem item){
        //use switch in terms of id
        switch(item.getItemId())
        {
            case R.id.settings:
                //click on setting go to setting page
                Intent i=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(i);
                break;

            case R.id.layout:
                //for signout
                auth.signOut();
                //when logout then move from mainactivity to signin activity
                Intent intent=new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);
                break;
                //Main to group chat activity
            case R.id.groupChat:
                Intent intentt=new Intent(MainActivity.this,GroupChatActivity.class);
                startActivity(intentt);
                break;
        }
        return true;
    }
}