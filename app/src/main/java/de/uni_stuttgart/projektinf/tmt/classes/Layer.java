package de.uni_stuttgart.projektinf.tmt.classes;

import android.graphics.Point;

import java.util.ArrayList;
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

    // anchor point (for sorting):
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

        this.anchorPoint = new Point(el, eb);

    }


    public void calculateRandomCirclePositionsInLayer() {

    }


    public void sortCircles() {

    }
}
