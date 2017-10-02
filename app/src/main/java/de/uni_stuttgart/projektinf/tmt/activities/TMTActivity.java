package de.uni_stuttgart.projektinf.tmt.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    public static final int NUMBEROFCIRCLES = 20;
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

        // use separate thread for the more computational circle position calculations:
        new TMTActivity.WorkTask(this).execute();
    }


    private class WorkTask extends AsyncTask<Void, Integer, Void> {
        Context context;

        private WorkTask(Context context) {
            this.context = context.getApplicationContext();
        }

        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params)
        {
            // calculate positions of circles using the DAC algorithm:
            calculateCirclePositionsDAC();

            // calculate random positions of circles (FOR TESTING):
            //calculateRandomCirclePositions();

            return null;
        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            // send circles to View to draw:
            tmtView.setCircles(circleList);

            TMTCalculator.startTMT();
        }

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
        Layer layer1 = new Layer(5,
                                    centerX - sixthOfScreenWidth, centerX + sixthOfScreenWidth,
                                    centerX - sixthOfScreenWidth, centerX + sixthOfScreenWidth,
                                    centerY - sixthOfScreenHeight, centerY + sixthOfScreenHeight,
                                    centerY - sixthOfScreenHeight, centerY + sixthOfScreenHeight);
        layerList.add(layer1);
        Layer layer2 = new Layer(7,
                                    centerX - 2*sixthOfScreenWidth, centerX - sixthOfScreenWidth,
                                    centerX + sixthOfScreenWidth, centerX + 2*sixthOfScreenWidth,
                                    centerY - 2*sixthOfScreenHeight, centerY - sixthOfScreenHeight,
                                    centerY + sixthOfScreenHeight, centerY + 2*sixthOfScreenHeight);
        layerList.add(layer2);
        Layer layer3 = new Layer(8,
                                    0, centerX - 2*sixthOfScreenWidth,
                                    centerX + 2*sixthOfScreenWidth, screenWidth,
                                    0, centerY - 2*sixthOfScreenHeight,
                                    centerY + 2*sixthOfScreenHeight, screenHeight);
        layerList.add(layer3);

        // generate circles in each layer and sort them:
        for (int i = 0; i < NUMBEROFLAYERS; i++){
            // calculate random positions within the layers:
            layerList.get(i).calculateAllRandomCirclesInLayer(screenWidth, screenHeight);
            // sort the positions using anchor point:
            layerList.get(i).sortCircles();
        }


        // ------------ COMBINE PHASE: -------------------------------------------------------------

        for (int i = NUMBEROFLAYERS - 1; i > 0; i--){
            // test for intersections:
            Circle badCircle = testAdjacentLayersIntersect(layerList.get(i), layerList.get(i-1));
            // as long as there are intersections, fix these and check again:
            while ( badCircle != null ){
                // get sequence numbers of old bad circle:
                int seqCreated = badCircle.getSequenceNumberWhenCreated();
                int seqLayer = badCircle.getSequenceNumberLayer();
                // delete old bad circle from lists:
                layerList.get(i).getLayerCircleList().remove(badCircle);
                // generate new random circle:
                Circle newCircle = layerList.get(i).getRandomCircleInLayer(screenWidth, screenHeight);
                // give new circle sequence numbers of the old:
                newCircle.setSequenceNumberWhenCreated(seqCreated);
                newCircle.setSequenceNumberLayer(seqLayer);
                // add new circle to the layer list:
                layerList.get(i).getLayerCircleList().add(newCircle);

                // then sort again:
                layerList.get(i).sortCircles();

                // after the fixings, check again if there are any other intersections:
                badCircle = testAdjacentLayersIntersect(layerList.get(i), layerList.get(i-1));
            }
        }

        // now find the pivot circles in the layers and rearrange the layer paths:
        for (int i = 0; i < NUMBEROFLAYERS - 1; i++) {
            // get the last circle of the inner layer:
            List<Circle> innerLayerList = layerList.get(i).getLayerCircleList();
            int last = innerLayerList.size() - 1;
            Circle lastInInnerLayer = innerLayerList.get(last);

            List<Circle> outerLayerList = layerList.get(i + 1).getLayerCircleList();

            // get the pivot circle in outer layer:
            Circle pivotCircle = findPivotCircle(lastInInnerLayer, innerLayerList, outerLayerList);

            // rearrange outer layer sequence according to pivot circle:
            int pivotIndex = outerLayerList.indexOf(pivotCircle);
            Log.i("bla", "outerLayerListSize: " + outerLayerList.size());
            Log.i("bla", "PivotIndex: " + pivotIndex);
            if (pivotIndex != 0) {
                List<Circle> backEndPart = new ArrayList<Circle>(outerLayerList.subList(0, pivotIndex));
                outerLayerList.addAll(backEndPart);
                outerLayerList.subList(0, pivotIndex).clear();
            }
        }

        // finally connect all layers by giving global sequence numbers and adding to global circle
        // list:
        int globalIndex = 1;
        for (int i = 0; i < NUMBEROFLAYERS; i++) {
            for(Circle c : layerList.get(i).getLayerCircleList()) {
                c.setSequenceNumberGlobal(globalIndex);
                globalIndex++;
                circleList.add(c);
            }
        }

    }

    private Circle findPivotCircle(Circle lastInInnerLayer, List<Circle> innerLayerList, List<Circle> outerLayerList) {
        // find pivot circle by testing for intersections:
        testPivotLoop:
        for(Circle testPivot : outerLayerList) {
            // first test against the segments in outer layer:
            for (int j = 0; j < outerLayerList.size() - 1; j++) {
                Circle c1 = outerLayerList.get(j);
                Circle c2 = outerLayerList.get(j + 1);
                // skip checkings containing the pivot itself:
                if (!c1.equals(testPivot))
                    if (!c2.equals(testPivot))
                        // check the (non-pivot containing) segment:
                        if ( testSegmentsIntersect(testPivot, lastInInnerLayer, c1, c2) )
                            continue testPivotLoop;

            }
            // then test against the segments in inner layer:
            for (int j = 0; j < innerLayerList.size() - 1; j++) {
                Circle c1 = innerLayerList.get(j);
                Circle c2 = innerLayerList.get(j + 1);
                // skip checkings containing the lastInInnerLayer circle itself:
                if (!c1.equals(lastInInnerLayer))
                    if (!c2.equals(lastInInnerLayer))
                        // check the (non-pivot containing) segment:
                        if ( testSegmentsIntersect(testPivot, lastInInnerLayer, c1, c2) )
                            continue testPivotLoop;
            }
            return testPivot;
        }
        return null;
    }

    private Circle testAdjacentLayersIntersect(Layer layer1, Layer layer2) {
        // go through all segments of the layer path:
        for (int i = 0; i < layer1.getLayerCircleList().size() - 1; i++){
            Circle c1 = layer1.getLayerCircleList().get(i);
            Circle c2 = layer1.getLayerCircleList().get(i+1);
            // test this segment with all other segments of layer2:
            for (int j = 0; j < layer2.getLayerCircleList().size() - 1; j++){
                Circle circ1 = layer2.getLayerCircleList().get(j);
                Circle circ2 = layer2.getLayerCircleList().get(j+1);
                if ( testSegmentsIntersect(c1, c2, circ1, circ2) )
                    return c1;

            }
        }
        // otherwise return null:
        return null;
    }

    private boolean testSegmentsIntersect(Circle c1, Circle c2, Circle circ1, Circle circ2){
        // bounding box of segment between c1 and c2 for pre-test:
        Rect r1 = getBoundingBox(c1, c2);
        // bounding box of segment between circ1 and circ2 for pre-test:
        Rect r2 = getBoundingBox(circ1, circ2);

        // first do a pre-test if the bounding boxes intersect:
        if (Rect.intersects(r1, r2)){
            // if yes, there is a possibility the segments intersect...
            // therefore do a mathematical exact test:
            float slope1 = (float)(c1.getPosY() - c2.getPosY()) / (float)(c1.getPosX() - c2.getPosX());
            float slope2 = (float)(circ1.getPosY() - circ2.getPosY()) / (float)(circ1.getPosX() - circ2.getPosX());
            float yInt1 = c1.getPosY() - slope1 * c1.getPosX();
            float yInt2 = circ1.getPosY() - slope2 * circ1.getPosX();
            // intersection point via formula:
            int intX = (int) ((yInt2 - yInt1) / (slope1 - slope2));

            // test if intersection point is within both segments:
            if ( Math.min(c1.getPosX(), c2.getPosX()) - Circle.RADIUS < intX )
                if ( intX < Math.max(c1.getPosX(), c2.getPosX()) + Circle.RADIUS)
                    if ( Math.min(circ1.getPosX(), circ2.getPosX()) - Circle.RADIUS < intX )
                        if ( intX < Math.max(circ1.getPosX(), circ2.getPosX()) + Circle.RADIUS )
                            // intersection happens:
                            return true;
        }
        // otherwise nothing happened:
        return false;
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
                    newCircle.setSequenceNumberGlobal(i+1);
                    circleList.add(newCircle);
                }

            }
        }


    }

    private Rect getBoundingBox(Circle c1, Circle c2){
        return new Rect(    Math.min(c1.getPosX(), c2.getPosX()),
                            Math.min(c1.getPosY(), c2.getPosY()),
                            Math.max(c1.getPosX(), c2.getPosX()),
                            Math.max(c1.getPosY(), c2.getPosY())    );
    }

    public static void TMTCompleted() {
        // stop timer:
        TMTCalculator.finishTMT();
        // takes user to the next activity:
        Intent viewResultsIntent = new Intent(thisContext, ViewResultsActivity.class);
        thisContext.startActivity(viewResultsIntent);
    }
}