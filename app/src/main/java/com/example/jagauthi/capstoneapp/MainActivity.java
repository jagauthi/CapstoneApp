package com.example.jagauthi.capstoneapp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.rosjava.android_remocons.common_tools.apps.RosAppActivity;

import org.ros.android.view.VirtualJoystickView;
import org.ros.namespace.NameResolver;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.io.IOException;

public class MainActivity extends RosAppActivity {

    private VirtualJoystickView virtualJoystickView;
    private Messenger messenger;

    private EditText editText;
    private Button messengerButton;

    public MainActivity() {
        super("android teleop", "android teleop");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setDashboardResource(R.id.top_bar);
        setMainWindowResource(R.layout.main);
        super.onCreate(savedInstanceState);

        virtualJoystickView = (VirtualJoystickView) findViewById(R.id.virtual_joystick);

        editText = (EditText) findViewById(R.id.messenger_text);
        messenger = new Messenger(editText.getContext());
        messengerButton = (Button)findViewById(R.id.messenger_button);
        messengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    public void sendMessage()
    {
        messenger.setSending(true);
        messenger.setMessage(editText.getText().toString());
        editText.setText("");
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {

        super.init(nodeMainExecutor);

        try {
            java.net.Socket socket = new java.net.Socket(getMasterUri().getHost(), getMasterUri().getPort());
            java.net.InetAddress local_network_address = socket.getLocalAddress();
            socket.close();
            NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(local_network_address.getHostAddress(), getMasterUri());

            String joyTopic = "cmd_vel";

            NameResolver appNameSpace = getMasterNameSpace();
            joyTopic = appNameSpace.resolve(joyTopic).toString();
            virtualJoystickView.setTopicName(joyTopic);

            nodeMainExecutor.execute(messenger, nodeConfiguration.setNodeName("android/messenger"));
            nodeMainExecutor.execute(virtualJoystickView, nodeConfiguration.setNodeName("android/virtual_joystick"));
        }
        catch (IOException e) {
            Log.d("tag", "Oh nooooooo");
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(0,0,0,"Kill");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case 0:
                onDestroy();
                break;
        }
        return true;
    }
}
