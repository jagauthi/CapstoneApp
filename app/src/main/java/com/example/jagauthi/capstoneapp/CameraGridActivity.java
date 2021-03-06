package com.example.jagauthi.capstoneapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.rosjava.android_remocons.common_tools.apps.RosAppActivity;

import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.io.IOException;

public class CameraGridActivity extends RosAppActivity {

    Button cama1, cama2, camb1, camb2;
    Button c, d, e, f, g, h, i, j;
    Button undoButton, submitButton;
    TextView sourceText, goalText;
    LinearLayout receivingBackground;

    private StringListener listenerFromCama1;
    private StringListener listenerFromCama2;
    private StringListener listenerFromCamb1;
    private StringListener listenerFromCamb2;
    private Messenger messengerToCama1;
    private Messenger messengerToCama2;
    private Messenger messengerToCamb1;
    private Messenger messengerToCamb2;

    String robotName;
    String startPosition, goalPosition;
    int pickStage;
    boolean waitingForStartPositions, receivedStartPositions;

    public CameraGridActivity() {
        super("android teleop", "android teleop");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_grid);

        robotName = getIntent().getExtras().getString("robotName");
        pickStage = 1;
        startPosition = "";
        goalPosition = "";
        waitingForStartPositions = false;
        receivedStartPositions = false;

        sourceText = (TextView)findViewById(R.id.sourceText);
        goalText = (TextView)findViewById(R.id.goalText);
        receivingBackground = (LinearLayout)findViewById(R.id.receivingBackground);

        c = (Button)findViewById(R.id.button2);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("c");
            }
        });

        d = (Button)findViewById(R.id.button3);
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("d");
            }
        });

        e = (Button)findViewById(R.id.button5);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("e");
            }
        });

        f = (Button)findViewById(R.id.button8);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("f");
            }
        });

        g = (Button)findViewById(R.id.button9);
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("g");
            }
        });

        h = (Button)findViewById(R.id.button12);
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("h");
            }
        });

        i = (Button)findViewById(R.id.button14);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("i");
            }
        });

        j = (Button)findViewById(R.id.button15);
        j.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseButton("j");
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

        listenerFromCama1 = new StringListener(cama1.getContext(), "cama1ToAppValidStart", this);
        listenerFromCama2 = new StringListener(cama1.getContext(), "cama2ToAppValidStart", this);
        listenerFromCamb1 = new StringListener(cama1.getContext(), "camb1ToAppValidStart", this);
        listenerFromCamb2 = new StringListener(cama1.getContext(), "camb2ToAppValidStart", this);
        messengerToCama1 = new Messenger(cama1.getContext(), "AppReqVStartscama1");
        messengerToCama2 = new Messenger(cama1.getContext(), "AppReqVStartscama2");
        messengerToCamb1 = new Messenger(cama1.getContext(), "AppReqVStartscamb1");
        messengerToCamb2 = new Messenger(cama1.getContext(), "AppReqVStartscamb2");

    }

    public void doSomethingWithMessage(std_msgs.String message) {
        //Going to receive multiple messages of the form "x,y"
        String[] newString = message.toString().split(",");
        final int x = Integer.parseInt(newString[0]);
        final int y = Integer.parseInt(newString[1]);
        Button newButton = new Button(cama1.getContext());
        newButton.setText("" + x + ", " + y);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickStartLocation(x, y);
            }
        });
        receivingBackground.addView(newButton);
    }

    public void pickStartLocation(int x, int y) {
        //Remember to turn receiving background off or clear it or something
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
        if(pickStage == 2) {
            String source = sourceText.getText().toString();
            if(source.equals("c")) {
                messengerToCama1.setMessage("north");
                messengerToCama1.setSending(true);
                waitingForStartPositions = true;
                receivingBackground.setVisibility(View.VISIBLE);
            }
            else if(source.equals("e")) {
                messengerToCama1.setMessage("west");
                messengerToCama1.setSending(true);
                waitingForStartPositions = true;
                receivingBackground.setVisibility(View.VISIBLE);
            }
            else if(source.equals("d")) {
                messengerToCamb1.setMessage("north");
                messengerToCamb1.setSending(true);
                waitingForStartPositions = true;
                receivingBackground.setVisibility(View.VISIBLE);
            }
            else if(source.equals("f")) {
                messengerToCamb1.setMessage("east");
                messengerToCamb1.setSending(true);
                waitingForStartPositions = true;
                receivingBackground.setVisibility(View.VISIBLE);
            }
            else if(source.equals("g")) {
                messengerToCama2.setMessage("west");
                messengerToCama2.setSending(true);
                waitingForStartPositions = true;
                receivingBackground.setVisibility(View.VISIBLE);
            }
            else if(source.equals("i")) {
                messengerToCama2.setMessage("south");
                messengerToCama2.setSending(true);
                waitingForStartPositions = true;
                receivingBackground.setVisibility(View.VISIBLE);
            }
            else if(source.equals("h")) {
                messengerToCamb2.setMessage("east");
                messengerToCamb2.setSending(true);
                waitingForStartPositions = true;
                receivingBackground.setVisibility(View.VISIBLE);
            }
            else if(source.equals("j")) {
                messengerToCamb2.setMessage("south");
                messengerToCamb2.setSending(true);
                waitingForStartPositions = true;
                receivingBackground.setVisibility(View.VISIBLE);
            }
            else if(source.equals("cama1") || source.equals("cama2") ||
                    source.equals("camb1") || source.equals("camb2")){
                //Do something? Like skip the steps for getting start positions and
                //start being able to pick the goal location?
            }
        }
        else if(pickStage == 3) {
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

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        super.init(nodeMainExecutor);

        try {
            java.net.Socket socket = new java.net.Socket(getMasterUri().getHost(), getMasterUri().getPort());
            java.net.InetAddress local_network_address = socket.getLocalAddress();
            socket.close();
            NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(local_network_address.getHostAddress(), getMasterUri());

            nodeMainExecutor.execute(listenerFromCama1, nodeConfiguration.setNodeName("android/listenerFromCama1"));
            nodeMainExecutor.execute(listenerFromCama2, nodeConfiguration.setNodeName("android/listenerFromCama2"));
            nodeMainExecutor.execute(listenerFromCamb1, nodeConfiguration.setNodeName("android/listenerFromCamb1"));
            nodeMainExecutor.execute(listenerFromCamb2, nodeConfiguration.setNodeName("android/listenerFromCamb2"));
            nodeMainExecutor.execute(messengerToCama1, nodeConfiguration.setNodeName("android/messengerToCama1"));
            nodeMainExecutor.execute(messengerToCama2, nodeConfiguration.setNodeName("android/messengerToCama2"));
            nodeMainExecutor.execute(messengerToCamb1, nodeConfiguration.setNodeName("android/messengerToCamb1"));
            nodeMainExecutor.execute(messengerToCamb2, nodeConfiguration.setNodeName("android/messengerToCamb2"));
        }
        catch (IOException e) {
            Log.d("tag", "Oh nooooooo");
            e.printStackTrace();
        }
    }
}