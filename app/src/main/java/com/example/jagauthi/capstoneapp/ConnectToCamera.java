package com.example.jagauthi.capstoneapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rosjava.android_remocons.common_tools.apps.RosAppActivity;

import org.apache.commons.lang.time.StopWatch;
import org.jboss.netty.buffer.ChannelBuffer;
import org.ros.android.view.VirtualJoystickView;
import org.ros.namespace.NameResolver;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectToCamera extends RosAppActivity {

    sensor_msgs.Image image;

    ImageView imageView;

    private Messenger messenger;
    private ImageListener listener;

    private Button connectButton;

    private VirtualJoystickView virtualJoystickView;

    byte[] imageByteArray = new byte[921600];

    boolean updateImageByte = true;

    Runnable updateImage = new Runnable() {
        @Override
        public void run() {
            if(imageByteArray != null) {
                updateImageByte = false;

                final int w = 640;
                final int h = 480;
                final int n = w * h;
                int red, green, blue, pixelARGB;
                final int [] buf = new int[n*3];
                for (int y = 0; y < h; y++) {
                    final int yw = y * w;
                    for (int x = 0; x < w; x++) {
                        int i = yw + x;
                        // Calculate 'pixelARGB' here.
                        red = imageByteArray[i++] & 0xFF;
                        green = imageByteArray[i++] & 0xFF;
                        blue = imageByteArray[i++]& 0xFF;
                        pixelARGB = 0xFF000000 | (red << 16)| (green << 8) | blue;
                        buf[i] = pixelARGB;
                    }
                }
//            InputStream in = new ByteArrayInputStream(imageByteArray);
//            int[] pixelData = new int[imageByteArray.length];
//            for(int i = 0;i < pixelData.length;i++){
//                pixelData[i] = imageByteArray[i];
//            }

//                Bitmap image = Bitmap.createBitmap(pixelData, 640, 480, Bitmap.Config.ARGB_8888);
                Bitmap image = Bitmap.createBitmap(buf, 640, 480, Bitmap.Config.ARGB_8888);
//            Bitmap bmp = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
//            ByteBuffer buffer = ByteBuffer.wrap(imageByteArray, 0, imageByteArray.length-1);
//            bmp.copyPixelsFromBuffer(buffer);
//            Bitmap bmp = BitmapFactory.
//            Bitmap bMap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
//            imageView.setImageAlpha(0);
                imageView.setImageBitmap(image);
                updateImageByte = true;
                imageView.postDelayed(updateImage, 100);
            }
        }
    };

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

        virtualJoystickView = (VirtualJoystickView) findViewById(R.id.virtual_joystick);
        //connectButton = (Button)findViewById(R.id.connect_button);
        //connectButton.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        sendMessage();
        //    }
        //});
        messenger = new Messenger(virtualJoystickView.getContext(), "cameraConnect");
        listener = new ImageListener(virtualJoystickView.getContext(), "getImage", this);

        imageView = (ImageView) findViewById(R.id.image);
        displayImage();
        if(!imageView.postDelayed(updateImage, 100)){
            Log.v("Nope", "Nope");
        }


        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        messenger = new Messenger(connectButton.getContext(), "CameraConnect");
        listener = new ImageListener(connectButton.getContext(), "GetImage", this);
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
        Log.d("Tagggggggg", "Here's the byte array's first element: " + byteArray[41]);
        Log.d("Tagggggggg", "Original array size: " + byteArray.length);
        if(updateImageByte) {
            this.imageByteArray = Arrays.copyOfRange(byteArray, byteArray.length-921600, byteArray.length);
            Log.v("Image byte array size", Integer.toString(imageByteArray.length));
        }
//        return byteArray;
//        ImageView imageView = (ImageView) findViewById(R.id.image);
//        Bitmap bMap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        imageView.setImageBitmap(bMap);
    }
    public ImageView getImageView(){
        return (ImageView) findViewById(R.id.image);
    }
    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {

        super.init(nodeMainExecutor);

        try {
            java.net.Socket socket = new java.net.Socket(getMasterUri().getHost(), getMasterUri().getPort());
            java.net.InetAddress local_network_address = socket.getLocalAddress();
            socket.close();
            NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(local_network_address.getHostAddress(), getMasterUri());

            String joyTopic = remaps.get("cmd_vel");

            NameResolver appNameSpace = getMasterNameSpace();
            joyTopic = appNameSpace.resolve(joyTopic).toString();
            virtualJoystickView.setTopicName(joyTopic);

            nodeMainExecutor.execute(messenger, nodeConfiguration.setNodeName("android/messenger"));
            nodeMainExecutor.execute(listener, nodeConfiguration.setNodeName("android/imageListener"));
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



    public void GoToNext(View v){
        if(imageByteArray != null) {
            updateImageByte = false;
            Log.v("GoToNext", Integer.toString(imageByteArray.length));

            final int w = 640;
            final int h = 480;
            final int n = w * h;
            int red, green, blue, pixelARGB;
            final int [] buf = new int[n*3];
            for (int y = 0; y < h; y++) {
                final int yw = y * w;
                for (int x = 0; x < w; x++) {
                    int i = yw + x;
                    // Calculate 'pixelARGB' here.
                    red = imageByteArray[i++] & 0xFF;
                    green = imageByteArray[i++] & 0xFF;
                    blue = imageByteArray[i++]& 0xFF;
                    pixelARGB = 0xFF000000 | (red << 16)| (green << 8) | blue;
                    buf[i] = pixelARGB;
                }
            }
//            InputStream in = new ByteArrayInputStream(imageByteArray);
//            int[] pixelData = new int[imageByteArray.length];
//            for(int i = 0;i < pixelData.length;i++){
//                pixelData[i] = imageByteArray[i];
//            }

            Bitmap image = Bitmap.createBitmap(buf, 640, 480, Bitmap.Config.ARGB_8888);
//            Bitmap bmp = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
//            ByteBuffer buffer = ByteBuffer.wrap(imageByteArray, 0, imageByteArray.length-1);
//            bmp.copyPixelsFromBuffer(buffer);
//            Bitmap bMap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
//            imageView.setImageAlpha(0);
//            imageView.setRotation(imageView.getRotation() + 90);
            imageView.setImageBitmap(image);
            updateImageByte = true;
        }


        //test code
//        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.a1);
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] bytes = stream.toByteArray();
//
//        Bitmap bMap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        imageView.setImageBitmap(bMap);


    }

    public void displayImage(){
//        if(prevImageByteArray != imageByteArray){
//            prevImageByteArray = imageByteArray;
//            ImageView imageView = (ImageView) findViewById(R.id.image);
        if(imageByteArray != null) {
            updateImageByte = false;
            Log.v("GoToNext", Integer.toString(imageByteArray.length));

            final int w = 640;
            final int h = 480;
            final int n = w * h;
            int red, green, blue, pixelARGB;
            final int [] buf = new int[n*3];
            for (int y = 0; y < h; y++) {
                final int yw = y * w;
                for (int x = 0; x < w; x++) {
                    int i = yw + x;
                    // Calculate 'pixelARGB' here.
                    red = imageByteArray[i++] & 0xFF;
                    green = imageByteArray[i++] & 0xFF;
                    blue = imageByteArray[i++]& 0xFF;
                    pixelARGB = 0xFF000000 | (red << 16)| (green << 8) | blue;
                    buf[i] = pixelARGB;
                }
            }
//            InputStream in = new ByteArrayInputStream(imageByteArray);
//            int[] pixelData = new int[imageByteArray.length];
//            for(int i = 0;i < pixelData.length;i++){
//                pixelData[i] = imageByteArray[i];
//            }

            Bitmap image = Bitmap.createBitmap(buf, 640, 480, Bitmap.Config.ARGB_8888);
//            Bitmap bmp = Bitmap.createBitmap(640, 480, Bitmap.Config.ARGB_8888);
//            ByteBuffer buffer = ByteBuffer.wrap(imageByteArray, 0, imageByteArray.length-1);
//            bmp.copyPixelsFromBuffer(buffer);
//            Bitmap bMap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
//            imageView.setImageAlpha(0);
//            imageView.setRotation(imageView.getRotation() + 90);
            imageView.setImageBitmap(image);
            updateImageByte = true;
        }
//        }
    }
}
