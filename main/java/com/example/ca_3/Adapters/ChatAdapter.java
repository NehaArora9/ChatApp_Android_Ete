package com.example.ca_3.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ca_3.Models.MessageModel;
import com.example.ca_3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{
//intiliaze the type of sender and viewer
    int SENDER_VIEW_TYPE =1;
    int RECEIVER_VIEW_TYPE =2;
    String recId;

    //2 parameter constructor(generate)
    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;

    }
//create constructor of above 3 method that we initialize
    public ChatAdapter(int SENDER_VIEW_TYPE, int RECEIVER_VIEW_TYPE, String recId) {
        this.SENDER_VIEW_TYPE = SENDER_VIEW_TYPE;
        this.RECEIVER_VIEW_TYPE = RECEIVER_VIEW_TYPE;
        this.recId = recId;
    }

    ArrayList<MessageModel> messageModels;
    Context context;

//alt enter(implement 3 method)
    @NonNull
    @Override
    //if viewtype= sender then layout(sender) else Layout(reciever)
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      if(viewType == SENDER_VIEW_TYPE)
      {
          View view= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
          return new SenderViewVolder(view);

      }
        else
      {
          View view= LayoutInflater.from(context).inflate(R.layout.sample_reciver,parent,false);
          return new RecieverViewVolder(view);
      }
    }
//condition : if the pos of user=firebase userid then return sender type else receiver type
    @Override
    public int getItemViewType(int position) {
        if(messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return 1;
        }
        else
        {
            return 2;
        }


    }
//To identify the message of sender( already logined) and receiver
    //Set the message of sender and receiver to the created layout(green and white)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    final MessageModel messageModel=messageModels.get(position);
    //for deletion (when we click on particular message then alert will show to ask from user to delete this message or not
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this message?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //instance of database
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                           //create the id of user and reciver
                            String senderRoom=FirebaseAuth.getInstance().getUid() + recId;
                            database.getReference().child("chats").child(senderRoom)
                                    .child(messageModel.getMessageId())
                                    .setValue(null);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
    });
    if(holder.getClass() == SenderViewVolder.class)
    {
        Log.d("tag","mmm--->"+messageModel.getMessage());
        ((SenderViewVolder)holder).senderMsg.setText(messageModel.getMessage());
    }
    else
    {
        ((RecieverViewVolder)holder).recieverMsg.setText(messageModel.getMessage());
    }
    }
///return the model size of message
    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    //create recieverViewHolder
    public class RecieverViewVolder extends RecyclerView.ViewHolder {
        TextView recieverMsg,recieverTime;
        public RecieverViewVolder(@NonNull View itemView) {
            super(itemView);
            recieverMsg=itemView.findViewById(R.id.recieverText);
            recieverTime=itemView.findViewById(R.id.recieverTime);

        }
    }
    //create sender viewHolder
    public class SenderViewVolder extends RecyclerView.ViewHolder {
        TextView senderMsg,senderTime;
        public SenderViewVolder(@NonNull View itemView) {
            super(itemView);
            senderMsg=itemView.findViewById(R.id.senderText);
            senderTime=itemView.findViewById(R.id.senderTime);

        }
    }
}
