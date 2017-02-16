package com.example.jagauthi.capstoneapp;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawListener implements OnTouchListener {

    static float endX, endY;
    DrawView drawView;

    public DrawListener(DrawView drawView)
    {
        endX = 0f;
        endY = 0f;
        this.drawView = drawView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == 0)
        {
            endX = event.getX();
            endY = event.getY();
        }
        else if(event.getAction() == 1)
        {
            drawView.drawNewLine(endX, endY);
        }
        return true;
    }
}
