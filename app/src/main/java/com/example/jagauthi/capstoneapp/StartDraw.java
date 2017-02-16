package com.example.jagauthi.capstoneapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartDraw extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_draw);

        Intent intent = getIntent();
        String hostname = intent.getStringExtra("Hostname");
        String username = intent.getStringExtra("Username");
        String password = intent.getStringExtra("Password");

        Button undoButton = (Button) findViewById(R.id.btnundo);
        Button activateButton = (Button) findViewById(R.id.btnactivate);
        Button submitButton = (Button) findViewById(R.id.btnsubmit);
        DrawView.setUndoButton(undoButton);
        DrawView.setActivateButton(activateButton);
        DrawView.setSubmitButton(submitButton);
    }
}
