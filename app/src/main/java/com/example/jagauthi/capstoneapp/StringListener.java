package com.example.jagauthi.capstoneapp;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.github.rosjava.android_remocons.common_tools.apps.RosAppActivity;

import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

/**
 * Created by jagauthi on 4/19/2017.
 */

public class StringListener implements NodeMain {
    private String topicName;
    Subscriber subscriber;
    RosAppActivity main;

    public StringListener(Context context, String topicName, RosAppActivity main) {
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
        subscriber = connectedNode.newSubscriber(this.topicName, "std_msgs/String");
        subscriber.addMessageListener(new MessageListener() {
            @Override
            public void onNewMessage(Object o) {
                std_msgs.String string = (std_msgs.String)o;
                if(main.getClass() == CameraGridActivity.class) {
                    CameraGridActivity connector = (CameraGridActivity)main;
                    connector.doSomethingWithMessage(string);
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