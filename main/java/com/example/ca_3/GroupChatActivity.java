package com.example.ca_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.ca_3.Adapters.ChatAdapter;
import com.example.ca_3.Models.MessageModel;
import com.example.ca_3.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {
    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //hide the toolbar
        getSupportActionBar().hide();
        //click on back arrow then shift to main activity
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.d("tag",database.toString());
        //create arraylist
        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        //create sender id
        final String senderId = FirebaseAuth.getInstance().getUid();
        //by default we set the group name as friends Group
        binding.userName.setText("Friends Group");

        //set adapter
        final ChatAdapter adapter = new ChatAdapter(messageModels, this);
        binding.chatRecyclerView.setAdapter(adapter);
        //set the layout
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        //get message from database and show on recyclerView
        database.getReference().child("Group Chat")
                //take value from DB
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //one message at a time
                        messageModels.clear();

                        //user keep sending and reciever keep receiving the message
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            MessageModel model=dataSnapshot.getValue(MessageModel.class);
                            messageModels.add(model);


                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //send message in group chatting
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //message convert string
                final String message=binding.etMessage.getText().toString();
                //create model(message save in model)backend part
                final MessageModel model=new MessageModel(senderId,message);
                //set the time
                model.setTimestamp(new Date().getTime());
                //after sending edittext will become blank
                binding.etMessage.setText("");
                //create the node of group chat
                database.getReference().child("Group Chat")
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {



                    }
                });

            }
        });
    }
}