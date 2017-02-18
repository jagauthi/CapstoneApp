package com.example.jagauthi.capstoneapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

public class ImageListener extends RelativeLayout implements NodeMain {
    private String topicName;
    Subscriber subscriber;
    sensor_msgs.Image image;
    ConnectToCamera main;

    public ImageListener(Context context, String topicName, ConnectToCamera main) {
        super(context);
        this.topicName = topicName;
        this.main = main;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public GraphName getDefaultNodeName() {
        return GraphName.of("androidMessenger");
    }

    public void onStart(ConnectedNode connectedNode) {
        subscriber = connectedNode.newSubscriber(this.topicName, "sensor_msgs/Image");
        subscriber.addMessageListener(new MessageListener() {
            @Override
            public void onNewMessage(Object o) {
                image = (sensor_msgs.Image)o;
                main.doSomethingWithImage(image);
//                ImageView imageView = (ImageView) findViewById(R.id.image);
//                ImageView imageView = main.getImageView();
//                Bitmap bMap = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
//                imageView.setImageBitmap(bMap);
            }
        });
    }

    public void onShutdown(Node node) {

    }

    public void onShutdownComplete(Node node) {

    }

    public void onError(Node node, Throwable throwable) {

    }
}