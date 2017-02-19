package com.example.jagauthi.capstoneapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.github.rosjava.android_remocons.common_tools.apps.RosAppActivity;

import org.jboss.netty.buffer.ChannelBuffer;
import org.ros.android.view.VirtualJoystickView;
import org.ros.namespace.NameResolver;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.io.IOException;
import java.util.Arrays;

public class ConnectToRobot extends RosAppActivity {

    sensor_msgs.Image image;

    ImageView imageView;

    private Messenger messenger;
    private ImageListener listener;

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
                Bitmap image = Bitmap.createBitmap(buf, 640, 480, Bitmap.Config.ARGB_8888);
                imageView.setImageBitmap(image);
                updateImageByte = true;
                imageView.postDelayed(updateImage, 100);
            }
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

        virtualJoystickView = (VirtualJoystickView) findViewById(R.id.virtual_joystick);
        messenger = new Messenger(virtualJoystickView.getContext(), "cameraConnect");
        listener = new ImageListener(virtualJoystickView.getContext(), "getImage", this);

        imageView = (ImageView) findViewById(R.id.image);
        displayImage();
        if(!imageView.postDelayed(updateImage, 100)){
            Log.v("Nope", "Nope");
        }
        listener = new ImageListener(imageView.getContext(), "GetImage", this);
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

    public void displayImage(){
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
            Bitmap image = Bitmap.createBitmap(buf, 640, 480, Bitmap.Config.ARGB_8888);
            imageView.setImageBitmap(image);
            updateImageByte = true;
        }
    }
}
