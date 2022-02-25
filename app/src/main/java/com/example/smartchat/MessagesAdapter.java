package com.example.smartchat;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class MessagesAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND=1;
    int ITEM_RECEIVE=2;
    String senderID;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList, String senderID) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
        this.senderID = senderID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND){

            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item, parent, false);
            return new SenderViewsHolder(view);

        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout_item, parent, false);
            return new ReceiverViewsHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages= messagesArrayList.get(position);

        if (holder.getClass() == SenderViewsHolder.class){


            SenderViewsHolder viewsHolder = (SenderViewsHolder) holder;
            viewsHolder.senderMessageTxt.setText(messages.getMessage());



        }else{
            ReceiverViewsHolder viewsHolder = (ReceiverViewsHolder) holder;
            viewsHolder.senderMessageTxt.setText(messages.getMessage());




        }
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);
        if (senderID.equals(messages.getSenderID()))
        {

            return ITEM_SEND;
        }else{

            return ITEM_RECEIVE;
        }
    }

    class SenderViewsHolder extends RecyclerView.ViewHolder{

        TextView senderMessageTxt;
        public SenderViewsHolder(@NonNull View itemView) {
            super(itemView);


            senderMessageTxt = itemView.findViewById(R.id.MessageTxt);
        }
    }

    class ReceiverViewsHolder extends RecyclerView.ViewHolder{

        TextView senderMessageTxt;
        public ReceiverViewsHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageTxt = itemView.findViewById(R.id.MessageTxt);
        }
    }
}
