package com.example.smartchat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.notificationViewHolder>{

    Context context;
    ArrayList<Messages> messagesArrayList;
    String passedData [] = new String[5];
    public String senderImg, senderName;

    public NotificationAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public notificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notificationchat_layout, parent, false);
        return new notificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull notificationViewHolder holder, int position) {
        Messages messages = messagesArrayList.get(position);
        holder.chatSenderName.setText(messages.getSenderName());
        holder.chatSenderMessage.setText(messages.message);
        String timeStampString = convertTime(messages.getTimeStamp());
        holder.timeStamp.setText(timeStampString.substring(10,timeStampString.length()-3));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_Intent = new Intent(context, ChatActivity.class);
                passedData [0] = messages.senderID.toString();
                passedData [1] = messages.getSenderName();
                new_Intent.putExtra("chatData",messages.getSenderID());
                new_Intent.putExtra("receiveName", passedData[1]);
                context.startActivity(new_Intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    class notificationViewHolder extends RecyclerView.ViewHolder{


        TextView chatSenderName, chatSenderMessage;
        TextView timeStamp;
        public notificationViewHolder(@NonNull View itemView) {
            super(itemView);

            chatSenderName = itemView.findViewById(R.id.chatSenderName);
            chatSenderMessage = itemView.findViewById(R.id.chatSenderMessage);
            timeStamp = itemView.findViewById(R.id.timeStamp);
        }
    }
    public String convertTime(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }
}
