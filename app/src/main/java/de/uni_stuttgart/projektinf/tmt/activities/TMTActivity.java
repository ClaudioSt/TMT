package de.uni_stuttgart.projektinf.tmt.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.uni_stuttgart.projektinf.tmt.R;
import de.uni_stuttgart.projektinf.tmt.classes.Circle;
import de.uni_stuttgart.projektinf.tmt.classes.Layer;
import de.uni_stuttgart.projektinf.tmt.helper_classes.TMTCalc;
import de.uni_stuttgart.projektinf.tmt.helper_classes.TMTView;

/**
 * The TMT Activity.
 * Here the user takes the actual Trail Making Test.
 *
 */
public class TMTActivity extends AppCompatActivity {

    //public static final int NUMBEROFCIRCLES = 25;
    public static final int NUMBEROFCIRCLES = 10;
    public static int currentCircleNumber = 1;
    List<Circle> circleList = new ArrayList<Circle>();
    private TMTView tmtView;
    private static Context thisContext;
    private static TMTCalc TMTCalculator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmt);
        tmtView = (TMTView)findViewById(R.id.tmt_view);
        thisContext = this;

        // hide the action bar:
        getSupportActionBar().hide();
        // hide the status bar:
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // create helper object for the many calculations:
        TMTCalculator = new TMTCalc();

        // calculate positions of circles using the DAC algorithm:
        //calculateCirclePositionsDAC();

        // calculate random positions of circles:
        calculateRandomCirclePositions();

        // send circles to View to draw:
        tmtView.setCircles(circleList);

        TMTCalculator.startTMT();
    }


    /**
     * Method calculateCirclePositionsDAC uses the DAC-algorithm introduced in the paper by Zeng et
     * al to generate the circles and their global sequence.
     * It uses a divide-and-combine approach to first create circles in some regions of the display
     * called layers (with an intern layer sequence). It then combines these intern layer sequences
     * to create a global circle sequence, which the TMT algorithm then uses.
     *
     * @see Circle
     * @see Layer
     */
    private void calculateCirclePositionsDAC(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;


        // ------------ DIVIDE PHASE: --------------------------------------------------------------

        final int NUMBEROFLAYERS = 3;
        List<Layer> layerList = new ArrayList<Layer>();

        int sixthOfScreenWidth = screenWidth/6;
        int sixthOfScreenHeight = screenHeight/6;
        int centerX = screenWidth/2;
        int centerY = screenHeight/2;

        // create the layers:
        Layer layer1 = new Layer(8,
                                    centerX, centerX - sixthOfScreenWidth,
                                    centerX, centerX + sixthOfScreenWidth,
                                    centerY, centerY - sixthOfScreenHeight,
                                    centerY, centerY + sixthOfScreenHeight);
        layerList.add(layer1);
        Layer layer2 = new Layer(8,
                                    centerX - sixthOfScreenWidth, centerX - 2*sixthOfScreenWidth,
                                    centerX + sixthOfScreenWidth, centerX + 2*sixthOfScreenWidth,
                                    centerY - sixthOfScreenHeight, centerY - 2*sixthOfScreenHeight,
                                    centerY + sixthOfScreenHeight, centerY + 2*sixthOfScreenHeight);
        layerList.add(layer2);
        Layer layer3 = new Layer(9,
                                    centerX - 2*sixthOfScreenWidth, 0,
                                    centerX + 2*sixthOfScreenWidth, screenWidth,
                                    centerY - 2*sixthOfScreenHeight, 0,
                                    centerY + 2*sixthOfScreenHeight, screenHeight);
        layerList.add(layer3);


        for (int i = 1; i <= NUMBEROFLAYERS; i++){
            // calculate random positions within the layers:
            layerList.get(i).calculateRandomCirclePositionsInLayer(screenWidth, screenHeight);
            // sort the positions using anchor point:
            layerList.get(i).sortCircles();
        }


        // ------------ COMBINE PHASE: -------------------------------------------------------------

        for (int i = NUMBEROFLAYERS; i > 1; i--){
            // test for intersections:
            List<Circle> intersectionCircles = testLayersIntersect(layerList.get(i), layerList.get(i-1));
            // as long as there are intersections, fix these and check again:
            while ( ! intersectionCircles.isEmpty() ){




                // after the fixings, check again if there are any other intersections:
                intersectionCircles = testLayersIntersect(layerList.get(i), layerList.get(i-1));
            }

        }


    }

    private List<Circle> testLayersIntersect(Layer layer1, Layer layer2) {
        // go through all segments of the layer path:
        for (int i = 0; i < layer1.getLayerCircleList().size() - 1; i++){
            Circle c1 = layer1.getLayerCircleList().get(i);
            Circle c2 = layer1.getLayerCircleList().get(i+1);
            // bounding box of segment between c1 and c2 for pre-test:
            Rect r1 = new Rect( Math.min(c1.getPosX(), c2.getPosX()),
                                Math.min(c1.getPosY(), c2.getPosY()),
                                Math.max(c1.getPosX(), c2.getPosX()),
                                Math.max(c1.getPosY(), c2.getPosY()));
            // test this segment with all other segments of layer2:
            for (int j = 0; j < layer2.getLayerCircleList().size() - 1; j++){
                Circle circ1 = layer1.getLayerCircleList().get(i);
                Circle circ2 = layer1.getLayerCircleList().get(i+1);
                // bounding box of segment between circ1 and circ2 for pre-test:
                Rect r2 = new Rect( Math.min(circ1.getPosX(), circ2.getPosX()),
                                    Math.min(circ1.getPosY(), circ2.getPosY()),
                                    Math.max(circ1.getPosX(), circ2.getPosX()),
                                    Math.max(circ1.getPosY(), circ2.getPosY()));

                // first do a pre-test if the bounding boxes intersect:
                if (Rect.intersects(r1, r2)){
                    // if yes, there is a possibility the segments intersect...
                    // therefore do a mathematical exact test:
                    float slope1 = (c1.getPosY() - c2.getPosY()) / (c1.getPosX() - c2.getPosX());
                    float slope2 = (circ1.getPosY() - circ2.getPosY()) / (circ1.getPosX() - circ2.getPosX());
                    float yInt1 = c1.getPosY() - slope1 * c1.getPosX();
                    float yInt2 = circ1.getPosY() - slope2 * circ1.getPosX();
                    // intersection point via formula:
                    int intX = (int) ((yInt2 - yInt1) / (slope1 - slope2));
                    // test if intersection point is within both segments:
                    if ( Math.min(c1.getPosX(), c2.getPosX()) <= intX )
                        if ( intX <= Math.max(c1.getPosX(), c2.getPosX()) )
                            if ( Math.min(circ1.getPosX(), circ2.getPosX()) <= intX )
                                if ( intX <= Math.max(circ1.getPosX(), circ2.getPosX()) )
                                    // intersection happens, return the corresponding circles:
                                    return ( new ArrayList<Circle>(Arrays.asList(c1,c2)) );
                }

            }

        }
        return null;
    }


    /**
     * Method calculateRandomCirclePositions generates the circles and their global sequence.
     * It simply generates random circles and takes their "creation sequence" as the global
     * sequence to use in the TMT algorithm.
     * (This leads to intersections and was only used as a simplified way for testing purposes!)
     *
     */
    private void calculateRandomCirclePositions(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;


        int randX = 0;
        int randY = 0;

        // iterate to find all circle positions:
        for (int i = 0; i < NUMBEROFCIRCLES; i++){
            boolean foundPos = false;
            // find random position at least radius-many pixels away from all other circles:
            while (!foundPos)
            {
                randX = (int)( Math.random() * (screenWidth - 2*Circle.RADIUS) ) + Circle.RADIUS;
                randY = (int)( Math.random() * (screenHeight - 2*Circle.RADIUS) ) + Circle.RADIUS;
                Point randomPoint = new Point(randX, randY);


                //test if position is far away enough from others (via euclid distance):
                boolean distanceIsOk = true;
                for(Circle otherCircle : circleList) {
                    if (otherCircle.getDistanceToPoint(randomPoint) < 4*Circle.RADIUS){
                        distanceIsOk = false;
                        break;
                    }
                }

                if (distanceIsOk){
                    foundPos = true;
                    Circle newCircle = new Circle(randX, randY, (i+1));
                    newCircle.sequenceNumberGlobal = (i+1);
                    circleList.add(newCircle);
                }

            }
        }


    }


    public static void TMTCompleted() {
        // stop timer:
        TMTCalculator.finishTMT();
        // takes user to the next activity:
        Intent viewResultsIntent = new Intent(thisContext, ViewResultsActivity.class);
        thisContext.startActivity(viewResultsIntent);
    }
}