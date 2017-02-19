package com.example.jagauthi.capstoneapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChooseAction extends AppCompatActivity {

    EditText robotName;
    EditText cameraName;
    Button connectRobot;
    Button connectCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_action);

        robotName = (EditText)findViewById(R.id.RobotName);
    }

    public void connectToRobot(View view) {
        Intent intent = new Intent(this, ConnectToCamera.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra("NameOfExtra", message);
        startActivity(intent);
    }

    public void connectToRobot(View view) {
        Intent intent = new Intent(this, ConnectToCamera.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra("NameOfExtra", message);
        startActivity(intent);
    }
}
