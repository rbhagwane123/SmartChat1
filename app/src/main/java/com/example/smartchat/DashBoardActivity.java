package com.example.smartchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DashBoardActivity extends AppCompatActivity {


    RecyclerView chatsBlockView;
    NotificationAdapter noteAdapter;
    SwipeRefreshLayout refreshLayout;;
    public String ReceiverNumber;
    String personNumber;
    Dialog dialog;
    Button CANCEL,LOGOUT;

    public ArrayList<Messages> messagesArrayList;
    public ArrayList<Long> timeArrayList;
    public Map<String, Long> timeValues;

    FirebaseDatabase database;
    DatabaseReference messageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        Intent login = getIntent();
        personNumber = login.getStringExtra("phoneNumber");

        SessionManager sessionManager = new SessionManager(DashBoardActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> ReceiverData = sessionManager.getUserDetailsFromSession();
        String name = ReceiverData.get(SessionManager.KEY_FULLNAME);

        refreshLayout = findViewById(R.id.refreshLayout);
        dialogCreationMore();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent chatActivity = new Intent(DashBoardActivity.this, DashBoardActivity.class);
                chatActivity.putExtra("phoneNumber", personNumber);
                startActivity(chatActivity);
                refreshLayout.setRefreshing(false);
            }
        });

        ReceiverNumber = ReceiverData.get(SessionManager.KEY_PHONENUMBER);

        database = FirebaseDatabase.getInstance("https://smartchat-8345e-default-rtdb.firebaseio.com/");
        messagesArrayList = new ArrayList<>();

        timeArrayList = new ArrayList<>();
        timeValues = new HashMap<>();
        chatCall();

        chatsBlockView = findViewById(R.id.chatsBlockView);
        chatsBlockView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        noteAdapter = new NotificationAdapter(DashBoardActivity.this, messagesArrayList);
        chatsBlockView.setAdapter(noteAdapter);
    }
    private void chatCall() {
        final String[] chckPerson = new String[1];
        Date date = new Date();

        DatabaseReference keyReference = database.getReference("Chats");
        keyReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    chckPerson[0] = dataSnapshot.getKey();

                    if (chckPerson[0].substring(0,10).equals(ReceiverNumber))
                    {
                        messageReference = dataSnapshot.getRef().child("messages");
                        messageReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren())
                                {
                                    String senderID = dataSnapshot1.child("senderID").getValue(String.class);
                                    String receiverID = dataSnapshot1.child("receiverID").getValue(String.class);

                                    long currentTime = date.getTime();

                                    if (receiverID.equals(ReceiverNumber))
                                    {
                                        long time = dataSnapshot1.child("timeStamp").getValue(long.class);
                                        timeArrayList.add(time);
                                    }
                                }

                                for (DataSnapshot dataSnapshot2 : snapshot.getChildren()){
                                    String receiverID = dataSnapshot2.child("receiverID").getValue(String.class);
                                    if (receiverID.equals(ReceiverNumber))
                                    {
                                        Long extractedTime = dataSnapshot2.child("timeStamp").getValue(long.class);
                                        long value = timeArrayList.get(timeArrayList.size()-1);
                                        if (extractedTime == value){
                                            String senderID = dataSnapshot2.child("senderID").getValue(String.class);
                                            String message = dataSnapshot2.child("message").getValue(String.class);
                                            String _senderName = dataSnapshot2.child("senderName").getValue(String.class);
                                            String _receiverName = dataSnapshot2.child("receiverName").getValue(String.class);

                                            Messages messages = new Messages(message, senderID, extractedTime, receiverID, _senderName, _receiverName);
                                            messagesArrayList.add(messages);
                                        }
                                    }
                                }
                                noteAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void dialogCreationMore() {
        dialog = new Dialog(DashBoardActivity.this);
        dialog.setContentView(R.layout.block_layout_design);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.info_layout_background_style));
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.setCanceledOnTouchOutside(true);

    }

    @Override
    public void onBackPressed() {

        dialog.show();
        CANCEL = dialog.findViewById(R.id.CANCEL);
        LOGOUT = dialog.findViewById(R.id.LOGOUT);
        CANCEL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        LOGOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardActivity.this, LoginActivity.class));
                dialog.dismiss();
            }
        });
    }
}