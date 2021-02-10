package com.doom.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doom.Models.Message;
import com.doom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{
    ArrayList<Message> msgList;

    public ChatAdapter(ArrayList<Message> msgList, Context context, String recieverId) {
        this.msgList = msgList;
        this.context = context;
        this.recieverId = recieverId;
    }

    Context context;
    String recieverId;

    public ChatAdapter(ArrayList<Message> msgList, Context context) {
        this.msgList = msgList;
        this.context = context;
    }

    int SENDER_VIEW_TYPE = 1;
    int RECIEVER_VIEW_TYPE = 2;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciever, parent, false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(msgList.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
            return SENDER_VIEW_TYPE;
        else
            return RECIEVER_VIEW_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final Message messageModel = msgList.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Delete Message")
                        .setMessage("Are you sure you want to delete the message")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() + recieverId;
                                database.getReference().child("chats").child(senderRoom)
                                        .child(messageModel.getMessageId())
                                        .setValue(null);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

                return false;
            }
        });

        if(holder.getClass() == SenderViewHolder.class)
        {
            ((SenderViewHolder)holder).senderMsg.setText(messageModel.getMessage());
        }
        else
        {
            ((RecieverViewHolder)holder).recieverMsg.setText(messageModel.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder {

        TextView recieverMsg, recieverTime;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverMsg = itemView.findViewById(R.id.messageReciever);
            recieverTime = itemView.findViewById(R.id.timeStampReciever);

        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView senderMsg, senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.messageSender);
            senderTime = itemView.findViewById(R.id.timeStampSender);

        }
    }
}
