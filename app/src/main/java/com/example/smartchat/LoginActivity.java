package com.example.smartchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText Password, phoneNumber;
    Button LoginBtn;
    NotificationManager nmgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Password = findViewById(R.id.Password);
        phoneNumber = findViewById(R.id.phoneNumber);
        LoginBtn = findViewById(R.id.loginBtn);

        nmgr=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel nchannel=new NotificationChannel("Notification1","My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            nchannel.enableVibration(true);
            nchannel.enableLights(true);
            nchannel.setDescription("Notification from MyActivity");
            nmgr.createNotificationChannel(nchannel);
        }

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumber.getText().toString() != null && Password.getText().toString() != null){
                    Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNumber").equalTo(phoneNumber.getText().toString());
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                String _pass = snapshot.child(phoneNumber.getText().toString()).child("password").getValue(String.class);

                                if (_pass.equals(Password.getText().toString()))
                                {
                                    String _name = snapshot.child(phoneNumber.getText().toString()).child("personName").getValue(String.class);
                                    String _DOB = snapshot.child(phoneNumber.getText().toString()).child("personDOB").getValue(String.class);

                                    Intent chatActivity = new Intent(LoginActivity.this, DashBoardActivity.class);
                                    chatActivity.putExtra("phoneNumber", phoneNumber.getText().toString());
                                    SessionManager sessionManager = new SessionManager(LoginActivity.this,SessionManager.SESSION_USERSESSION);
                                    sessionManager.createLoginSession(_name, phoneNumber.getText().toString(), Password.getText().toString());
                                    NotificationCompat.Builder mybuilder=getNotifyBuilder();
                                    nmgr.notify(1,mybuilder.build());
                                    startActivity(chatActivity);
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "No such user", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });
    }
    private NotificationCompat.Builder getNotifyBuilder(){
        Intent intent =new Intent(this,Notified.class);
        PendingIntent pintent =PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder nBuilder =new NotificationCompat.Builder(this,"Notification1")
                .setContentTitle("Notification from Smart Chat")
                .setContentText("You have successful login")
                .setSmallIcon(R.drawable.ic_baseline_notifications_none_24)
                .setContentIntent(pintent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);
        return nBuilder;
    }
}