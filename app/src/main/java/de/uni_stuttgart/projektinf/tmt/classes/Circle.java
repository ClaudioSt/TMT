package de.uni_stuttgart.projektinf.tmt.classes;

import android.graphics.Color;

/**
 * The Circle Class.
 * This class is used to represent all the circles in the TMT.
 */
public class Circle {

    // Circle radius:
    public static final int RADIUS = 100;
    // Circle stroke width (thickness of the circle line):
    public static final float strokeWidth = 4.5f;
    // Circle color (of the circle line):
    public static final int color = Color.BLACK;
    // Circle content size (of the numbers and letters):
    public static final int contentTextSize = 50;
    // X-Position:
    private int posX;
    // Y-Position:
    private  int posY;
    // the content (i.e. 1,2,3,... or A,B,C,...):
    private String content;

    public Circle(int x, int y){
        this.posX = x;
        this.posY = y;
    }


    public int getPosX(){
        return posX;
    }
    public int getPosY(){
        return posY;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return content;
    }
}
