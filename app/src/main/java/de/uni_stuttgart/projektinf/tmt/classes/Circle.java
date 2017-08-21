package de.uni_stuttgart.projektinf.tmt.classes;

import android.graphics.Color;

import de.uni_stuttgart.projektinf.tmt.activities.TMTActivity;

/**
 * The Circle Class.
 * This class is used to represent all the circles in the TMT.
 */
public class Circle {

    // Circle radius:
    public static final int RADIUS = 100;
    // Circle stroke width (thickness of the circle line):
    public static final float strokeWidth = 4.5f;
    // Tolerance in pixels for checking if touched:
    public static final int TOLERANCE = 10;
    // Circle content size (of the numbers and letters):
    public static final int contentTextSize = 50;
    // X-Position:
    private int posX;
    // Y-Position:
    private  int posY;
    // Circle color (of the circle line):
    private int color = Color.BLACK;
    // the content (i.e. 1,2,3,... or A,B,C,...):
    private String content;
    // already touched by user?
    private boolean gotTouched = false;
    // the sequence number of the corresponding order:
    private int sequenceNumber;

    public Circle(int x, int y, int number){
        this.posX = x;
        this.posY = y;
        this.sequenceNumber = number;
    }


    public int getPosX(){
        return posX;
    }
    public int getPosY(){
        return posY;
    }
    public int getColor(){
        return color;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return content;
    }
    public void setTouched(){
        gotTouched = true;
        color = Color.GREEN;
    }
    public boolean gotTouched(){
        return gotTouched;
    }

    public void checkIfCorrect() {
        if (TMTActivity.currentCircleNumber == sequenceNumber)
        {
            TMTActivity.currentCircleNumber++;
            setTouched();

            // check if it is the last one:
            if (sequenceNumber == TMTActivity.NUMBEROFCIRCLES)
            {
                TMTActivity.TMTCompleted();
            }
        }
        else
        {
            // TODO: set red cross and jump back to last circle
            color = Color.RED;
        }


    }
}
