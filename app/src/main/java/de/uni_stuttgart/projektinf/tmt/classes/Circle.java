package de.uni_stuttgart.projektinf.tmt.classes;

/**
 * Created by Clemens on 23.07.2017.
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
