package com.example.smartchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Password = findViewById(R.id.Password);
        phoneNumber = findViewById(R.id.phoneNumber);
        LoginBtn = findViewById(R.id.loginBtn);

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
}