package de.uni_stuttgart.projektinf.tmt.classes;

import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;

import de.uni_stuttgart.projektinf.tmt.activities.TMTActivity;
import de.uni_stuttgart.projektinf.tmt.helper_classes.TMTView;

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
    private int sequenceNumberGlobal;
    private int sequenceNumberLayer;

    public Circle(int x, int y, int number){
        this.posX = x;
        this.posY = y;
        this.sequenceNumberGlobal = number;
    }


    public int getPosX(){
        return posX;
    }
    public int getPosY(){
        return posY;
    }
    public Point getPoint(){
        return new Point(posX, posY);
    }
    public int getSequenceNumberGlobal(){
        return sequenceNumberGlobal;
    }
    public int getColor(){
        return color;
    }
    public void setColor(int c){
        color = c;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return content;
    }
    public void setTouched(){
        gotTouched = true;
    }
    public boolean gotTouched(){
        return gotTouched;
    }

    public void checkIfCorrect(TMTView view) {
        // is it the correct one?
        if (TMTActivity.currentCircleNumber == sequenceNumberGlobal)
        {
            TMTActivity.currentCircleNumber++;
            setTouched();
            color = Color.GREEN;
            view.setPathStartingPoint(getPoint());


            // check if it is the last one:
            if (sequenceNumberGlobal == TMTActivity.NUMBEROFCIRCLES)
            {
                TMTActivity.TMTCompleted();
            }
        }
        // if NOT the correct one:
        else
        {
            // TODO: set red cross and jump back to last circle?
            color = Color.RED;
            TMTView.setWrongCircle(this);
            if (TMTView.DELETEWRONGPATH){
                view.resetDrawPath();

            }

        }


    }
    public int getDistanceToPoint(Point pt){
        return (int) Math.sqrt((posX - pt.x) * (posX - pt.x) + (posY - pt.y) * (posY - pt.y));

    }
}
