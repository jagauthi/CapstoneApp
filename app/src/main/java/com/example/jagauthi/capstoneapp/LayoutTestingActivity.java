package com.example.jagauthi.capstoneapp;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.DrawableUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LayoutTestingActivity extends AppCompatActivity {

    ImageView imageView;
    AnimationDrawable animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_testing);

        imageView = (ImageView) findViewById(R.id.image);
    }


    class Starter implements Runnable {
        public void run() {
            animation.start();
        }
    }

//    private void startAnimation(){
//        animation = new AnimationDrawable();
//        animation.addFrame(getResources().getDrawable(R.drawable.a1), 100);
//        animation.addFrame(getResources().getDrawable(R.drawable.a2), 100);
//        animation.addFrame(getResources().getDrawable(R.drawable.a3), 100);
//        animation.addFrame(getResources().getDrawable(R.drawable.b1), 100);
//        animation.addFrame(getResources().getDrawable(R.drawable.b2), 100);
//        animation.addFrame(getResources().getDrawable(R.drawable.b3), 100);
//        animation.addFrame(getResources().getDrawable(R.drawable.b4), 100);
//        animation.addFrame(getResources().getDrawable(R.drawable.beautifulart), 100);
//        animation.setOneShot(true);
//
//        ImageView imageView = (ImageView) findViewById(R.id.image);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(80, 90);
//        params.alignWithParent = true;
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//
//        imageView.setLayoutParams(params);
//        imageView.setImageDrawable(animation);
//        imageView.post(new Starter());

    public void StartTheThing(View v){



//        final List<Integer> images = new ArrayList<Integer>();
//
//        images.add(R.drawable.a1);
//        images.add(R.drawable.a2);
//        images.add(R.drawable.a3);
//        images.add(R.drawable.b1);
//        images.add(R.drawable.b2);
//        images.add(R.drawable.b3);
//        images.add(R.drawable.b4);
//        images.add(R.drawable.beautifulart);

        final List<Drawable> images = new ArrayList<>();

        images.add(getResources().getDrawable(R.drawable.a1));
        images.add(getResources().getDrawable(R.drawable.a2));
        images.add(getResources().getDrawable(R.drawable.a3));
        images.add(getResources().getDrawable(R.drawable.b1));
        images.add(getResources().getDrawable(R.drawable.b2));
        images.add(getResources().getDrawable(R.drawable.b3));
        images.add(getResources().getDrawable(R.drawable.b4));
        images.add(getResources().getDrawable(R.drawable.beautifulart));



        final Handler handler = new Handler();
        final Random random = new Random();
        handler.postDelayed(new Runnable() {
            public void run() {
                int randomNum = random.nextInt(6);
//                imageView.setImageResource(images.get(randomNum));
                imageView.setImageDrawable(images.get(randomNum));
                handler.postDelayed(this, 500);
            }
        }, 500);

//        animation = new AnimationDrawable();
//        animation.addFrame(getResources().getDrawable(R.drawable.a1), 100);
//        animation.addFrame(getResources().getDrawable(R.drawable.a2), 100);
//        animation.addFrame(getResources().getDrawable(R.drawable.a3), 100);
//        animation.addFrame(getResources().getDrawable(R.drawable.b1), 100);
//        animation.addFrame(getResources().getDrawable(R.drawable.b2), 100);
//        animation.addFrame(getResources().getDrawable(R.drawable.b3), 100);
//        animation.addFrame(getResources().getDrawable(R.drawable.b4), 100);
//        animation.addFrame(getResources().getDrawable(R.drawable.beautifulart), 100);
//        animation.setOneShot(true);
//
//        ImageView imageView = (ImageView) findViewById(R.id.image);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(80, 90);
//        params.alignWithParent = true;
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//
//        imageView.setLayoutParams(params);
//        imageView.setImageDrawable(animation);
//        for(int i = 0;i<100;i++){
//            imageView.post(new Starter());
//            Log.v("LayoutTestingActivity","Image thing finished?");
//        }




//
//        List<Integer> images = new ArrayList<>();
//
//        images.add(R.drawable.a1);
//        images.add(R.drawable.a2);
//        images.add(R.drawable.a3);
//        images.add(R.drawable.b1);
//        images.add(R.drawable.b2);
//        images.add(R.drawable.b3);
//        images.add(R.drawable.b4);
//        images.add(R.drawable.beautifulart);
//
////        imageView.setImageResource(R.drawable.b2);
//
//        for(int i = 0;i < 10; i++){
//            for(Integer image: images){
//                try {
//                    Thread.sleep(1000);
//                }catch (Exception e){
//                    Log.v("Exception", "whoops");
//                }
////                imageView.setBackground(d);
//                Log.v("LayoutTestingActivity", "Setting Image");
////                imageView.setImageDrawable(d);
//                imageView.setImageResource(image);
//            }
//        }
//        Log.v("LayoutTestingActivity", "Finished");


    }

}
