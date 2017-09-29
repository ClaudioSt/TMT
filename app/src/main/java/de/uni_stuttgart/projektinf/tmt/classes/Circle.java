package de.uni_stuttgart.projektinf.tmt.classes;

import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;

import de.uni_stuttgart.projektinf.tmt.activities.ChooseSequenceActivity;
import de.uni_stuttgart.projektinf.tmt.activities.TMTActivity;
import de.uni_stuttgart.projektinf.tmt.helper_classes.TMTView;

/**
 * The Circle Class.
 * This class is used to represent all the circles in the TMT.
 */
public class Circle {

    // Circle radius:
    public static final int RADIUS = 70;
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
    // the angle relative to the layer anchor point:
    int angleRelativeToAnchor;
    // the sequence numbers:
    private int sequenceNumberWhenCreated;
    public int sequenceNumberGlobal;
    int sequenceNumberLayer;


    public Circle(int x, int y, int number){
        this.posX = x;
        this.posY = y;
        this.sequenceNumberWhenCreated = number;
    }
    public Circle(Point pt, int number){
        this.posX = pt.x;
        this.posY = pt.y;
        this.sequenceNumberWhenCreated = number;
    }
    public Circle(Point pt){
        this.posX = pt.x;
        this.posY = pt.y;
    }


    /**
     * Method setContent sets the (to the user visible) content of the circles. Depending on the
     * chosen sequence order this varies.
     */
    private void setContent(){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] alphabetArray  = alphabet.toCharArray();

         /*
        ChooseSequenceActivity.sequence:
        1 = 1,2,3,4,5,6,...
        2 = A,B,C,D,E,F,...
        3 = 1,A,2,B,3,C,...
         */

        switch (ChooseSequenceActivity.sequence){
            case 1: this.content = "" + sequenceNumberGlobal;
                break;
            case 2: this.content = String.valueOf(alphabetArray[sequenceNumberGlobal - 1]);
                break;
            case 3: if (sequenceNumberGlobal % 2 == 0)
                this.content = String.valueOf(alphabetArray[sequenceNumberGlobal/2 - 1]);
            else
                this.content = "" + (int)Math.ceil(sequenceNumberGlobal / 2.0);
                break;

        }
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
    public void setSequenceNumberWhenCreated(int n){
        sequenceNumberWhenCreated = n;
    }
    public int getSequenceNumberWhenCreated(){
        return sequenceNumberWhenCreated;
    }
    public void setSequenceNumberGlobal(int n){
        sequenceNumberGlobal = n;
        setContent();
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
    public String getContent(){
        return content;
    }
    public void setTouched(){
        gotTouched = true;
    }
    public boolean gotTouched(){
        return gotTouched;
    }

    /**
     * Method isInsideTheCircle checks if a given point lays within this circle (or within the
     * tolerance).
     *
     * @param point
     */
    public boolean isInsideTheCircle(Point point){
        // calculate distance from circle center to point:
        int distance = getDistanceToPoint(point);
        // check if distance is ok and point is "really" inside the circle:
        if (distance < RADIUS + TOLERANCE)
            return true;
        else
            return false;
    }

    /**
     * Method checkIfCorrect is called in checkCircleTouch() in the TMTView class.
     * It tests if this circle is the correct circle in the sequence by comparing the circles
     * sequence number with the number of the current needed circle. According to the test
     * result appropriate measures are performed.
     *
     * @param view
     * @see TMTView
     */
    public void checkIfCorrect(TMTView view) {
        // is it the correct one regarding the sequence?
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

    /**
     * Method getDistanceToPoint returns the euclid distance from the center of this circle to the
     * given point.
     *
     * @param pt
     */
    public int getDistanceToPoint(Point pt){
        return (int) Math.sqrt((posX - pt.x) * (posX - pt.x) + (posY - pt.y) * (posY - pt.y));

    }
}
