package com.example.jagauthi.capstoneapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.rosjava.android_remocons.common_tools.apps.RosAppActivity;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

public class ImageListener extends RelativeLayout implements NodeMain {
    private String topicName;
    Subscriber subscriber;
    RosAppActivity main;

    public ImageListener(Context context, String topicName, RosAppActivity main) {
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
        //subscriber = connectedNode.newSubscriber(this.topicName, "sensor_msgs/CompressedImage");
        subscriber.addMessageListener(new MessageListener() {
            @Override
            public void onNewMessage(Object o) {
                sensor_msgs.Image image = (sensor_msgs.Image)o;
                //sensor_msgs.CompressedImage image = (sensor_msgs.CompressedImage)o;
                if(main.getClass() == ConnectToRobot.class) {
                    ConnectToRobot connector = (ConnectToRobot)main;
                    connector.doSomethingWithImage(image);
                    //connector.doSomethingWithCompressedImage(image);
                }
                else if(main.getClass() == ConnectToCamera.class) {
                    ConnectToCamera connector = (ConnectToCamera)main;
                    connector.doSomethingWithImage(image);
                    //connector.doSomethingWithCompressedImage(image);
                }
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