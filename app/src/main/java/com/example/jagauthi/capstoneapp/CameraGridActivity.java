package com.example.jagauthi.capstoneapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CameraGridActivity extends AppCompatActivity {

    Button cama1, cama2, camb1, camb2;
    Button a, b, c, d, e, f, g, h;
    Button undoButton, submitButton;
    TextView sourceText, goalText;

    String robotName;
    String startPosition, goalPosition;
    int pickStage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_grid);

        robotName = getIntent().getExtras().getString("robotName");
        pickStage = 1;
        startPosition = "";
        goalPosition = "";

        sourceText = (TextView)findViewById(R.id.sourceText);
        goalText = (TextView)findViewById(R.id.goalText);

        a = (Button)findViewById(R.id.button2);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("a");
            }
        });

        b = (Button)findViewById(R.id.button3);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("b");
            }
        });

        c = (Button)findViewById(R.id.button5);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("c");
            }
        });

        d = (Button)findViewById(R.id.button8);
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("d");
            }
        });

        e = (Button)findViewById(R.id.button9);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("e");
            }
        });

        f = (Button)findViewById(R.id.button12);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("f");
            }
        });

        g = (Button)findViewById(R.id.button14);
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("g");
            }
        });

        h = (Button)findViewById(R.id.button15);
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("h");
            }
        });

        cama1 = (Button)findViewById(R.id.button6);
        cama1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("cama1");
            }
        });

        cama2 = (Button)findViewById(R.id.button7);
        cama2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("cama2");
            }
        });

        camb1 = (Button)findViewById(R.id.button10);
        camb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("camb1");
            }
        });

        camb2 = (Button)findViewById(R.id.button11);
        camb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("camb2");
            }
        });

        undoButton = (Button)findViewById(R.id.undoButton);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undo();
            }
        });

        submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    public void chooseButton(String buttonName) {
        if(pickStage == 1) {
            startPosition = buttonName;
            pickStage = 2;
            sourceText.setText("Source: " + buttonName);
        }
        else if(pickStage == 2) {
            if (buttonName.equals("cama1") || buttonName.equals("cama2") || buttonName.equals("camb1") || buttonName.equals("camb2")) {
                goalPosition = buttonName;
                pickStage = 3;
                goalText.setText("Goal: " + buttonName);
            }
        }

    }

    public void undo() {
        if(pickStage == 2) {
            pickStage = 1;
            sourceText.setText("Source:");
            goalText.setText("Goal:");
        }
        else if(pickStage == 3) {
            pickStage = 2;
            goalText.setText("Goal:");
        }
    }

    public void submit() {
        pickStage = 1;
        sourceText.setText("Source:");
        goalText.setText("Goal:");
        Intent intent = new Intent(this, ConnectToCamera.class);
        intent.putExtra("robotName", robotName);
        intent.putExtra("startPosition", startPosition);
        intent.putExtra("goalPosition", goalPosition);
        startActivity(intent);
    }
}