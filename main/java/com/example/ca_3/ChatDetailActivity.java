package com.example.ca_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.ca_3.Adapters.ChatAdapter;
import com.example.ca_3.Models.MessageModel;
import com.example.ca_3.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {
ActivityChatDetailBinding binding;
FirebaseDatabase database;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //hide toolbar
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        DatabaseReference dbref = database.getReference("chats");

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(Collections.singleton(snapshot));
                Log.d("tag","--->"+arrayList.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //receiver id (userAdapter->chatDetailActivity)
        //make sender id final to make it global
       final String senderId= auth.getUid();
        String recieveId=getIntent().getStringExtra("userId");
        String userName=getIntent().getStringExtra("userName");
        String profilePic=getIntent().getStringExtra("profilePic");

        //set the above data
        binding.userName.setText(userName);
        //if no image of the user then set the image from drawable into profileImage
        Picasso.get().load(profilePic).placeholder(R.drawable.ic_avatar).into(binding.profileImage);
        //when we click on back arrow we will shift from chatActivity Page to the MainActivity page
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        //whrer our data comes from->messagemodel
        final ArrayList<MessageModel> messageModels=new ArrayList<>();
        //use adapter ->chatadapter (date comes from here) and set it over the adapter
        final ChatAdapter chatAdapter=new ChatAdapter(messageModels,this);
        binding.chatRecyclerView.setAdapter(chatAdapter);
//chatRecyclerView(id of recycler view) and set it over layout manager
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);
  //create the node of sender and receiver -> both side data
  final String senderRoom =senderId + recieveId;
  final String receiverRoom =recieveId + senderId;
  //take data from database  ,user logined(sender)
database.getReference().child("chats")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //1 message one at a time with updated time stamp (snapshot from database and set it over the recycler view)
                        messageModels.clear();
                        //user again and again send the message and receiver receive the message
                        for(DataSnapshot snapshot1: snapshot.getChildren())
                        {
                          MessageModel model=snapshot1.getValue(MessageModel.class);
                          //get id(message delete)
                          model.setMessageId(snapshot1.getKey());
                           messageModels.add(model);
                        }
                        //recycler view updated with given time stamp
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        //when we click on send button
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //user type convert string and go in mess
                String message =binding.etMessage.getText().toString();
                //2 parameter pass in database
                final  MessageModel model=new MessageModel(senderId,message);
                //take the message date and time
                model.setTimestamp(new Date().getTime());
                //when we send the typed message then the edit text of type message will become empty
                binding.etMessage.setText("");
                //create reference of chats
                //push->send message with updated time
                //and set the value in model
                //message send ->store database for sender and receiver
                database.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    database.getReference().child("chats")
                            .push()
                            .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                    }
                });

            }
        });


    }
}