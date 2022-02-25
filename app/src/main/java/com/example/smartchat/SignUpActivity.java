package com.example.smartchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    Button SignUp;
    EditText personName,personNumber,personDob,personPass;

    FirebaseAuth auth;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SignUp = findViewById(R.id.SignUp);
        personName= findViewById(R.id.personName);
        personNumber= findViewById(R.id.personNumber);
        personDob= findViewById(R.id.personDob);
        personPass= findViewById(R.id.personPass);

        database = FirebaseDatabase.getInstance("https://smartchat-8345e-default-rtdb.firebaseio.com/");

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (personName.getText().toString() != null &&
                        personNumber.getText().toString() != null &&
                        personDob.getText().toString() != null &&
                        personPass.getText().toString() != null){

                    DatabaseReference reference = database.getReference().child("Users").child(personNumber.getText().toString());
                    Users users = new Users(personName.getText().toString(), personNumber.getText().toString(), personDob.getText().toString(), personPass.getText().toString());
                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "values set", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, DashBoardActivity.class));
                            }
                        }
                    });

                }else{
                    Toast.makeText(SignUpActivity.this, "Fields left empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}