package com.example.jagauthi.capstoneapp;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rosjava.android_remocons.common_tools.apps.RosAppActivity;
import com.google.common.base.Preconditions;

import org.jboss.netty.buffer.ChannelBuffer;
import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.view.VirtualJoystickView;
import org.ros.namespace.NameResolver;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.io.IOException;
import java.util.Arrays;

import sensor_msgs.Image;

public class ConnectToCamera extends RosAppActivity {

    ImageView imageView;
    DrawView drawView;
    TextView loadingText;

    private ImageListener listener;
    private Messenger messenger;

    Bitmap whatWellDraw;
    Bitmap whatWellDraw2;

    Runnable updateImage = new Runnable() {
        @Override
        public void run() {
            if(whatWellDraw != null) {
                imageView.setImageBitmap(whatWellDraw);
                loadingText.setVisibility(View.INVISIBLE);
            }
            imageView.postDelayed(updateImage, 100);

            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.width = (int)(params.height*1.33333);
            imageView.setLayoutParams(params);
            drawView.setLayoutParams(params);
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

        Button undoButton = (Button) findViewById(R.id.btnundo);
        Button activateButton = (Button) findViewById(R.id.btnactivate);
        Button submitButton = (Button) findViewById(R.id.btnsubmit);

        imageView = (ImageView) findViewById(R.id.imageview);
        drawView = (DrawView) findViewById(R.id.drawview);
        loadingText = (TextView) findViewById(R.id.loadingText);

        drawView.setSubmitButton(submitButton, this);
        drawView.setUndoButton(undoButton);
        drawView.setActivateButton(activateButton);

        listener = new ImageListener(imageView.getContext(), "getImage", this);
        messenger = new Messenger(imageView.getContext(), "sendGoal");

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
        //Log.d("DebuggingTag", "Start processing image");
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
        //Log.d("DebuggingTag", "Time for last frame: " + (System.currentTimeMillis()-startTime));
        return bitmap;
    }

    public void sendMessage(int x, int y)
    {
        messenger.setMessage("" + x + " " + y);
        messenger.setSending(true);
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        super.init(nodeMainExecutor);

        try {
            java.net.Socket socket = new java.net.Socket(getMasterUri().getHost(), getMasterUri().getPort());
            java.net.InetAddress local_network_address = socket.getLocalAddress();
            socket.close();
            NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(local_network_address.getHostAddress(), getMasterUri());

            nodeMainExecutor.execute(listener, nodeConfiguration.setNodeName("android/imageListener"));
            nodeMainExecutor.execute(messenger, nodeConfiguration.setNodeName("android/mesenger"));
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
