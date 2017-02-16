package com.example.jagauthi.capstoneapp;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import java.util.Timer;
import java.util.TimerTask;
import org.ros.namespace.GraphName;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;

public class Messenger extends RelativeLayout implements NodeMain {
    private Publisher<std_msgs.String> publisher;
    private Timer publisherTimer;
    private String topicName;
    boolean sending = false;
    String message;

    public Messenger(Context context, String topicName) {
        super(context);
        this.topicName = topicName;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public GraphName getDefaultNodeName() {
        return GraphName.of("androidMessenger");
    }

    public void onStart(ConnectedNode connectedNode) {
        this.publisher = connectedNode.newPublisher(this.topicName, "std_msgs/String");

        this.publisherTimer = new Timer();
        this.publisherTimer.schedule(new TimerTask() {
            public void run() {
                if(sending) {
                    std_msgs.String str = publisher.newMessage();
                    str.setData(message);
                    publisher.publish(str);
                    sending = false;
                }
            }
        }, 0L, 80L);
    }

    public void setSending(boolean val) {
        sending = val;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void onShutdown(Node node) {

    }

    public void onShutdownComplete(Node node) {
        this.publisherTimer.cancel();
        this.publisherTimer.purge();
    }

    public void onError(Node node, Throwable throwable) {

    }
}
