package de.uni_stuttgart.projektinf.tmt.classes;

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

    public Layer (int n ){
        this.numberOfCirclesInLayer = n;


    }


}
