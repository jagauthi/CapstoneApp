package com.example.jagauthi.capstoneapp;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rosjava.android_remocons.common_tools.apps.RosAppActivity;

import org.jboss.netty.buffer.ChannelBuffer;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectToCamera extends RosAppActivity {

    sensor_msgs.Image image;

    private Messenger messenger;
    private ImageListener listener;

    private Button connectButton;

    EditText editTextAddress;
    Button buttonConnect;
    TextView textPort;

    static final int SocketServerPORT = 60000;

    public ConnectToCamera() {
        super("android teleop", "android teleop");
        System.out.println("Test");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setDashboardResource(R.id.top_bar);
        setMainWindowResource(R.layout.activity_connect_to_camera);
        super.onCreate(savedInstanceState);

        ////////////////////////////////////////////////////////////////
        //textPort.setText("port: " + SocketServerPORT);
       // buttonConnect = (Button) findViewById(R.id.connect);
        //buttonConnect.setOnClickListener(new View.OnClickListener(){
        //    @Override
         //   public void onClick(View v) {
         //       ClientRxThread clientRxThread = new ClientRxThread("192.168.1.104", SocketServerPORT);
         //       clientRxThread.start();
         //   }});

        //////////////////////////////////////////////////////////////////
    
        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        messenger = new Messenger(connectButton.getContext(), "cameraConnect");
        listener = new ImageListener(connectButton.getContext(), "getImage", this);
    }

    public void sendMessage()
    {
        messenger.setMessage("I want to connect");
        messenger.setSending(true);
    }

    public void doSomethingWithImage(sensor_msgs.Image image)
    {
        this.image = image;
        ChannelBuffer buffer = this.image.getData();
        byte[] byteArray = buffer.array();
        Log.d("Tagggggggg", "Here's the byte array's first element: " + byteArray[0]);
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {

        super.init(nodeMainExecutor);

        try {
            java.net.Socket socket = new java.net.Socket(getMasterUri().getHost(), getMasterUri().getPort());
            java.net.InetAddress local_network_address = socket.getLocalAddress();
            socket.close();
            NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(local_network_address.getHostAddress(), getMasterUri());

            nodeMainExecutor.execute(messenger, nodeConfiguration.setNodeName("android/messenger"));
            nodeMainExecutor.execute(listener, nodeConfiguration.setNodeName("android/imageListener"));
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

    private class ClientRxThread extends Thread {
        String dstAddress;
        int dstPort;

        ClientRxThread(String address, int port) {
            dstAddress = address;
            dstPort = port;
        }

        @Override
        public void run() {
            Socket socket = null;
            try {
                socket = new Socket(dstAddress, dstPort);
                File file = new File(Environment.getExternalStorageDirectory(), "test.txt");

                byte[] bytes = new byte[1024];
                InputStream is = socket.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                int bytesRead = is.read(bytes, 0, bytes.length);
                bos.write(bytes, 0, bytesRead);
                bos.close();
                socket.close();
                Log.d("tag", "" + bytes[0]);

                ConnectToCamera.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ConnectToCamera.this, "Finished", Toast.LENGTH_LONG).show();
                    }});

            }
            catch (IOException e) {
                e.printStackTrace();

                final String eMsg = "Something wrong: " + e.getMessage();
                ConnectToCamera.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ConnectToCamera.this, eMsg, Toast.LENGTH_LONG).show();
                    }});

            }
            finally {
                if(socket != null){
                    try {
                        socket.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
