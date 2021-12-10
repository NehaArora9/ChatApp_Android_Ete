package com.example.ca_3.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ca_3.ChatDetailActivity;
import com.example.ca_3.R;
import com.example.ca_3.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    ArrayList<Users> list;
    Context context;


    //create constructor(rightclick->enerate ->constructor)

    public UsersAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    //implements methods(alt enter)
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_show_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //get the position of the user
        final Users users=list.get(position);
        //set profile pic by user
        Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.ic_avatar).into(holder.image);
       //take user name from user
        holder.userName.setText(users.getUserName());
        //get the data from the firebase
        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(FirebaseAuth.getInstance().getUid()+users.getUserId())
                //1 mess shown at top, 2 mess take the 2 position
                .orderByChild("timestamp")
                //limit is 1 mess only
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //if any mess in b/w
                        if(snapshot.hasChildren()){

                            for(DataSnapshot snapshot1:snapshot.getChildren()){
                              holder.lastMessage.setText(snapshot1.child("message").getValue().toString());
                              

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });




        //(image,user )send to next activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ChatDetailActivity.class);
                //send this data to the next activity
                //data of sender directly taken from firebase
                intent.putExtra("userId",users.getUserId());
                intent.putExtra("profilePic",users.getProfilepic());
                intent.putExtra("userName",users.getUserName());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView userName,lastMessage;
        //constructor(alt enter)
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.profile_image);
            userName=itemView.findViewById(R.id.userNameList);
            lastMessage=itemView.findViewById(R.id.lastMessage);



        }
    }
}
