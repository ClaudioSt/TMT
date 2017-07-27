package de.uni_stuttgart.projektinf.tmt.classes;

/**
 * The Circle Class.
 * This class is used to represent all the circles in the TMT.
 */

public class Circle {
    public static final int RADIUS = 100;
    private int posX;
    private  int posY;

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
}
