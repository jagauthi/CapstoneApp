package com.example.jagauthi.capstoneapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.net.ConnectException;
import java.util.ArrayList;

import static java.lang.Math.acos;

public class DrawView extends View {

    Paint paint = new Paint();
    ConnectToCamera connectToCamera;
    Line defaultLine;
    ArrayList<Line> lines;
    Point circlePoint;
    Button undoButton, activateButton, submitButton;
    Context theContext;
    boolean drawingPath = false;
    int goalX, goalY;

    public DrawView(Context context) {
        super(context);
        init(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        defaultLine = new Line(0, 1000, 200, 1000, 0.0);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20f);
        lines = new ArrayList<Line>();
        lines.add(defaultLine);
        circlePoint = new Point(0, 0);
        setOnTouchListener(new DrawListener(this));
        setBackgroundColor(Color.GRAY);
        theContext = context;
    }

    public void setUndoButton(Button button) {
        undoButton = button;
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLastLine();
            }
        });
    }

    public void setActivateButton(Button button) {
        activateButton = button;
        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawing();
            }
        });
    }

    public void setSubmitButton(Button button, ConnectToCamera ctc) {
        connectToCamera = ctc;
        submitButton = button;
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPathAsString();
            }
        });
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (drawingPath) {
            for (Line line : lines) {
                canvas.drawLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY(), paint);
                invalidate();
            }
        } else {
            canvas.drawCircle(circlePoint.x, circlePoint.y, 40, paint);
            invalidate();
        }
    }

    public void drawNewLine(float endX, float endY) {
        Line newLine = new Line(lines.get(lines.size() - 1).endX, lines.get(lines.size() - 1).endY, endX, endY, 0.0);
        double angle = calculateAngle(lines.get(lines.size() - 1), newLine);
        newLine.setStartAngle(angle);
        lines.add(newLine);
    }

    public void drawNewCircle(float endX, float endY) {
        circlePoint = new Point((int) endX, (int) endY);
        double xRatio = endX / this.getWidth();
        double yRatio = endY / this.getHeight();

        goalX = (int) (xRatio * 640);
        goalY = (int) (yRatio * 480);
        Log.d("DrawView", "Goal x: " + goalX);
        Log.d("DrawView", "Goal y: " + goalY);
    }

    public double calculateAngle(Line line1, Line line2) {
        double angle = 0.0;
        angle = acos((((line1.getLength() * line1.getLength()) + (line2.getLength() * line2.getLength()) -
                (Line.getDistanceBetween((int) line1.getStartX(), (int) line1.getStartY(), (int) line2.getEndX(), (int) line2.getEndY()) *
                        Line.getDistanceBetween((int) line1.getStartX(), (int) line1.getStartY(), (int) line2.getEndX(), (int) line2.getEndY())
                )) / (2 * line1.getLength() * line2.getLength())));

        angle *= (180 / Math.PI);
        angle = 180 - angle;

        float line1x = line1.getEndX() - line1.getStartX();
        float line1y = line1.getEndY() - line1.getStartY();
        float line2x = line2.getEndX() - line2.getStartX();
        float line2y = line2.getEndX() - line2.getStartX();
        if ((line1x * line2y - line1y * line2x) < 0)
            return -angle;
        else
            return angle;
    }

    public void removeLastLine() {
        if (lines.size() > 1)
            lines.remove(lines.size() - 1);
    }

    public void toggleDrawing() {
        if (drawingPath) {
            drawingPath = !drawingPath;
            activateButton.setText("Drawing Goal");
        } else {
            drawingPath = !drawingPath;
            activateButton.setText("Drawing Path");
        }
    }

    public void getPathAsString() {
        connectToCamera.sendMessage(goalX, goalY);
        /*
        String path = "1\\n"; //First character is ignored
        for(int i = 0; i < lines.size(); i++)
        {
            for(int forwards = 0; forwards < (lines.get(i).getLength() / 10); forwards++)
            {
                path += "1\\n";
            }
            if(i != lines.size()-1)//if there is a line after this one
            {
                for(int turns = 0; turns < Math.abs(lines.get(i+1).getStartAngle() / 10); turns++)
                {
                    if(lines.get(i+1).getStartAngle() < 0) //left turn
                        path += "2\\n";
                    else //right turn
                        path += "4\\n";
                }
            }
        }
        lines.clear();
        lines.add(defaultLine);
        path += "5";
        System.out.println("Heres the path...\n" + path);
        */
    }
}