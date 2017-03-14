package com.example.jagauthi.capstoneapp;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.github.rosjava.android_remocons.common_tools.apps.RosAppActivity;
import com.google.common.base.Preconditions;

import org.jboss.netty.buffer.ChannelBuffer;
import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.BitmapFromImage;
import org.ros.android.view.RosImageView;
import org.ros.android.view.VirtualJoystickView;
import org.ros.namespace.NameResolver;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.io.IOException;
import java.util.Arrays;

import sensor_msgs.Image;

public class ConnectToRobot extends RosAppActivity {

    String robotName;

    ImageView imageView;
    RosImageView rosImageView;

    private ImageListener listener;

    private VirtualJoystickView virtualJoystickView;

    Bitmap whatWellDraw;
    Bitmap whatWellDraw2;

    Runnable updateImage = new Runnable() {
        @Override
        public void run() {
            if(whatWellDraw != null) {
                imageView.setImageBitmap(whatWellDraw);
            }
            imageView.postDelayed(updateImage, 100);
        }
    };

    public ConnectToRobot() {
        super("android teleop", "android teleop");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setDashboardResource(R.id.top_bar);
        setMainWindowResource(R.layout.activity_connect_to_robot);
        super.onCreate(savedInstanceState);

        robotName = getIntent().getExtras().getString("robotName");

        virtualJoystickView = (VirtualJoystickView) findViewById(R.id.virtual_joystick);
        listener = new ImageListener(virtualJoystickView.getContext(), robotName + "Image", this);

        imageView = (ImageView) findViewById(R.id.image);
        rosImageView = (RosImageView) findViewById(R.id.rosimage);
        rosImageView.setMessageType("sensor_msgs.CompressedImage");
        rosImageView.setTopicName("getCompressedImage");
        rosImageView.setMessageToBitmapCallable(new BitmapFromImage());
        if(!imageView.postDelayed(updateImage, 100)){
            Log.v("Nope", "Nope");
        }
    }

    public void doSomethingWithImage(sensor_msgs.Image image)
    {
        whatWellDraw = convertImageToBitmap(image);
    }

    public void doSomethingWithCompressedImage(sensor_msgs.CompressedImage image)
    {
        BitmapFromCompressedImage thing = new BitmapFromCompressedImage();
        whatWellDraw2 = thing.call(image);
    }

    public Bitmap convertImageToBitmap(Image message) {
        long startTime = System.currentTimeMillis();
        Log.d("DebuggingTag", "Start");
        Preconditions.checkArgument(message.getEncoding().equals("rgb8"));
        Bitmap bitmap = Bitmap.createBitmap(message.getWidth(), message.getHeight(), Bitmap.Config.ARGB_8888);

        int step = message.getStep();
        ChannelBuffer data = message.getData();
        for(int x = 0; x < message.getWidth(); ++x) {
            for(int y = 0; y < message.getHeight(); ++y) {
                byte red = data.getByte(y * step + 3 * x);
                byte green = data.getByte(y * step + 3 * x + 1);
                byte blue = data.getByte(y * step + 3 * x + 2);
                bitmap.setPixel(x, y, Color.rgb(red & 255, green & 255, blue & 255));
            }
        }
        Log.d("DebuggingTag", "Time for last frame: " + (System.currentTimeMillis()-startTime));
        return bitmap;
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
}
