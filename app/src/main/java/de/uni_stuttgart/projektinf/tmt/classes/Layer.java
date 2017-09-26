package de.uni_stuttgart.projektinf.tmt.classes;

import android.graphics.Point;
import android.view.Display;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Layer Class.
 * This class is used to represent the layers that are used for the trail search.
 */
public class Layer {

    // number of circles in this layer:
    private int numberOfCirclesInLayer;

    // the circles in this layer:
    private List<Circle> circleListLayer = new ArrayList<Circle>();

    // all variables to describe the area of the layer:
    private int beginLeft;
    private int beginRight;
    private int endLeft;
    private int endRight;
    private int beginTop;
    private int beginBottom;
    private int endTop;
    private int endBottom;

    // anchor point of this layer (for sorting):
    private Point anchorPoint;


    public Layer (int n, int bl, int el, int br, int er, int bt, int et, int bb, int eb ){
        this.numberOfCirclesInLayer = n;
        this.beginLeft = bl;
        this.beginRight = br;
        this.endLeft = el;
        this.endRight = er;
        this.beginTop = bt;
        this.beginBottom = bb;
        this.endTop = et;
        this.endBottom = eb;

        // take bottom left point as anchor point (as suggested in the paper):
        this.anchorPoint = new Point(bl, eb);

    }

    /**
     * Method calculateRandomCirclePositionsInLayer generates random circles that lay within the
     * layer and are not too close to one another.
     *
     */
    public void calculateRandomCirclePositionsInLayer(int screenWidth, int screenHeight) {
        // iterate to find number of random circles needed:
        for (int i = 0; i < numberOfCirclesInLayer; i++){

            // find a random position that fulfills the criteria:
            boolean foundPos = false;
            while (!foundPos)
            {
                // at first take random position on the screen:
                int randX = (int)( Math.random() * (screenWidth - 2*Circle.RADIUS) ) + Circle.RADIUS;
                int randY = (int)( Math.random() * (screenHeight - 2*Circle.RADIUS) ) + Circle.RADIUS;
                Point randomPoint = new Point(randX, randY);

                // then check if this position is even in the layer:
                boolean isInLayer = testIfInLayer(randomPoint);

                //test if position is far away enough from others (via euclid distance):
                boolean distanceIsOk = true;
                for(Circle otherCircle : circleListLayer) {
                    if (otherCircle.getDistanceToPoint(randomPoint) < 3*Circle.RADIUS){
                        distanceIsOk = false;
                        break;
                    }
                }

                if (isInLayer & distanceIsOk){
                    foundPos = true;
                    Circle newCircle = new Circle(randomPoint, (i+1));
                    circleListLayer.add(newCircle);
                }

            }
        }
    }

    /**
     * Method sortCircles sorts the circles of this layer by angle relative to the anchor point and
     * assigns them the corresponding sequence number within the layer.
     */
    public void sortCircles() {
        // first set in which direction to sort:
        boolean sortClockwise = false;

        // calculate angle of every circle in this layer relative to the anchor point:
        for(Circle circle: circleListLayer) {
            float angle = (float) Math.toDegrees(Math.atan2(circle.getPosY() - anchorPoint.y,
                                                            circle.getPosX() - anchorPoint.x));
            if(angle < 0)
                angle += 360;

            circle.angleRelativeToAnchor = (int) angle;
        }

        // sort the layer circle list by angle:
        if (sortClockwise)
            Collections.sort(circleListLayer, (Circle c1, Circle c2) -> c1.angleRelativeToAnchor - c2.angleRelativeToAnchor);
        else
            Collections.sort(circleListLayer, (Circle c1, Circle c2) -> c2.angleRelativeToAnchor - c1.angleRelativeToAnchor);

        // set the layer sequence number for all circles in the layer:
        for(int i = 0; i < circleListLayer.size(); i++) {
            circleListLayer.get(i).sequenceNumberLayer = i;
        }
    }

    /**
     * Method testIfInLayer tests if a given point lays within the boundaries of the layer.
     *
     * @param point
     */
    public boolean testIfInLayer(Point point){
        boolean isInLayer = true;

        if (point.x < beginLeft)
            isInLayer = false;
        else if (point.x > endLeft)
            isInLayer = false;
        else if (point.x < beginRight)
            isInLayer = false;
        else if (point.x > endRight)
            isInLayer = false;
        else if (point.y < beginTop)
            isInLayer = false;
        else if (point.y > endTop)
            isInLayer = false;
        else if (point.y < beginBottom)
            isInLayer = false;
        else if (point.y > endBottom)
            isInLayer = false;

        return isInLayer;
    }

    public List<Circle> getLayerCircleList(){
        return circleListLayer;
    }

}
