package com.example.jagauthi.capstoneapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChooseAction extends AppCompatActivity {

    Button leftRobot1, leftRobot2;
    Button rightCamera1, rightCamera2, rightCamera3, rightCamera4;
    Button rightRobot1, rightRobot2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_action);

        leftRobot1 = (Button)findViewById(R.id.leftRobot1);
        leftRobot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToAvailableRobot("rob1");
            }
        });
        leftRobot2 = (Button)findViewById(R.id.leftRobot2);
        leftRobot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToAvailableRobot("rob2");
            }
        });

        rightCamera1 = (Button)findViewById(R.id.rightCamera1);
        rightCamera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToCamera("cama1");
            }
        });
        rightCamera2 = (Button)findViewById(R.id.rightCamera2);
        rightCamera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToCamera("cama2");
            }
        });
        rightCamera3 = (Button)findViewById(R.id.rightCamera3);
        rightCamera3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToCamera("camb1");
            }
        });
        rightCamera4 = (Button)findViewById(R.id.rightCamera4);
        rightCamera4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToCamera("camb2");
            }
        });

        rightRobot1 = (Button)findViewById(R.id.rightRobot1);
        rightRobot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToUnavailableRobot("rob1");
            }
        });
        rightRobot2 = (Button)findViewById(R.id.rightRobot2);
        rightRobot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectToUnavailableRobot("rob2");
            }
        });
    }

    public void connectToAvailableRobot(String robotName) {
        Intent intent = new Intent(this, ConnectToRobot.class);
        intent.putExtra("robotName", robotName);
        startActivity(intent);
    }

    public void connectToCamera(String cameraName) {
        Intent intent = new Intent(this, ConnectToCamera.class);
        intent.putExtra("cameraName", cameraName);
        startActivity(intent);
    }

    public void connectToUnavailableRobot(String robotName) {
        Intent intent = new Intent(this, ConnectToRobot.class);
        intent.putExtra("robotName", robotName);
        startActivity(intent);
    }
}
