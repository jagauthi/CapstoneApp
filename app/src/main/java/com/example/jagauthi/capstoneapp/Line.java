package com.example.jagauthi.capstoneapp;

public class Line {

    float startX, startY, endX, endY;
    double startAngle;

    public Line(float startX, float startY, float endX, float endY, double startAngle)
    {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.startAngle = startAngle;
    }

    public float getEndY() {
        return endY;
    }

    public void setEndY(int endY) { this.endY = endY; }

    public float getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public float getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public float getStartX() { return startX; }

    public double getStartAngle() { return startAngle; }

    public void setStartAngle(double startAngle) { this.startAngle = startAngle; }

    public float getLength()
    {
        double distance = Math.sqrt( (endX-startX)*(endX-startX) + (endY-startY)*(endY-startY) );
        return (float)distance;
    }

    public static float getDistanceBetween(int x1, int y1, int x2, int y2)
    {
        double distance = Math.sqrt( (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) );
        return (float)distance;
    }
}


