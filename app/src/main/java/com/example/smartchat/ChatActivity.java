package com.example.smartchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    TextView receiverName;
    RecyclerView messageAdapter;
    ImageButton chatBackBtn, dotMenu;
    CardView sendBtn;
    EditText textMessage;
    String personReceiverNumber;
    String senderUID;
    String receiverUID, whatToDo;
    String senderRoom;
    String receiverRoom;
    static ArrayList<Messages> messagesArrayList;
    MessagesAdapter onlyAdapter;
    public String senderName, receveName;
    ImageView call;
    ImageButton more;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();

        Intent chat = getIntent();
        personReceiverNumber = chat.getStringExtra("chatData");
        receveName = chat.getStringExtra("receiveName");

        Toast.makeText(ChatActivity.this, personReceiverNumber, Toast.LENGTH_SHORT).show();
        receiverName = findViewById(R.id.receiverName);
        textMessage = findViewById(R.id.textMessage);
        sendBtn = findViewById(R.id.sendBtn);
        chatBackBtn = findViewById(R.id.chatBackBtn);
        messageAdapter = findViewById(R.id.messageAdapter);
        call = findViewById(R.id.call);
        more = findViewById(R.id.more);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+personReceiverNumber));
                startActivity(intent);
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ChatActivity.this, v);
                popupMenu.setOnMenuItemClickListener(ChatActivity.this);
                popupMenu.inflate(R.menu.chat_menu);
                popupMenu.show();
            }
        });

        messagesArrayList = new ArrayList<>();
        chatBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent previous = new Intent(ChatActivity.this, DashBoardActivity.class);
                previous.putExtra("phoneNumber", senderUID);
                startActivity(previous);
            }

        });

        receiverName.setText(receveName);

        SessionManager sessionManager = new SessionManager(ChatActivity.this, SessionManager.SESSION_USERSESSION);
        HashMap<String, String> senderData = sessionManager.getUserDetailsFromSession();

        senderUID = senderData.get(SessionManager.KEY_PHONENUMBER);
        senderName = senderData.get(SessionManager.KEY_FULLNAME);
        receiverUID = personReceiverNumber;
        Query checkUserSender = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobileNumber").equalTo(senderUID);
        checkUserSender.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String _fullName = snapshot.child(senderUID).child("fullName").getValue(String.class);
                String _dob = snapshot.child(senderUID).child("d_o_b").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        onlyAdapter = new MessagesAdapter(ChatActivity.this, messagesArrayList, senderUID);
        messageAdapter.setAdapter(onlyAdapter);

        senderRoom = senderUID + receiverUID;
        receiverRoom = receiverUID + senderUID;
        chatReferenceCall();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textmessage = textMessage.getText().toString();

                if (textmessage.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Enter text", Toast.LENGTH_SHORT).show();
                    return;
                }
                textMessage.setText("");
                Date date = new Date();

                Messages messages = new Messages(textmessage, senderUID, date.getTime(), receiverUID, senderName, receveName);
                database = FirebaseDatabase.getInstance();
                database.getReference().child("Chats")
                        .child(senderRoom)
                        .child("messages")
                        .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            database.getReference().child("Chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    }
                });

            }
        });
    }


    public void chatReferenceCall() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference chatReference = database.getReference().child("Chats").child(senderRoom).child("messages");
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                onlyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.BlockUser:
                Toast.makeText(ChatActivity.this, "BlockUser called", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.clearChat:
                Toast.makeText(ChatActivity.this, "clearChat called", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.Settings:
                Toast.makeText(ChatActivity.this, "Settings called", Toast.LENGTH_SHORT).show();
                return true;


            default:
                return false;
        }
    }
}