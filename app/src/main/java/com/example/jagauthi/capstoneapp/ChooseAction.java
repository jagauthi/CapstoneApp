package com.example.jagauthi.capstoneapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChooseAction extends AppCompatActivity {

    Button connectRobot;
    Button connectCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_action);

        connectRobot = (Button)findViewById(R.id.ConnectToRobot);
        connectCamera = (Button)findViewById(R.id.ConnectToCamera);
        connectRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToRobot();
            }
        });
        connectCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToCamera();
            }
        });
    }

    public void connectToRobot() {
        Intent intent = new Intent(this, ConnectToRobot.class);
        startActivity(intent);
    }

    public void connectToCamera() {
        Intent intent = new Intent(this, ConnectToCamera.class);
        startActivity(intent);
    }
}
