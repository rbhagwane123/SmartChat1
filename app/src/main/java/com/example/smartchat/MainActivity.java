package com.example.smartchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start =(Button)findViewById(R.id.buttonstart);
        start.setOnClickListener(new View.OnClickListener() {
            Boolean isOnePressed = false, isSecondPlace = false;
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isOnePressed = true;
                start.setBackgroundColor(Color.GRAY);
                if (isSecondPlace) {
                    start.setBackgroundColor(Color.WHITE);
                    isSecondPlace = false;
                }
                Intent intent =new Intent(MainActivity.this,WelcomeActivity.class);
                startActivity(intent);
            }
        });
    }
}