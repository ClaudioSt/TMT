package de.uni_stuttgart.projektinf.tmt.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

import java.util.ArrayList;
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



    private void calculateCirclePositionsDAC(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        // ------------ DIVIDE PHASE: --------------------------------------------------------------

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
        Layer layer2 = new Layer(8,
                                    centerX - sixthOfScreenWidth, centerX - 2*sixthOfScreenWidth,
                                    centerX + sixthOfScreenWidth, centerX + 2*sixthOfScreenWidth,
                                    centerY - sixthOfScreenHeight, centerY - 2*sixthOfScreenHeight,
                                    centerY + sixthOfScreenHeight, centerY + 2*sixthOfScreenHeight);
        Layer layer3 = new Layer(9,
                                    centerX - 2*sixthOfScreenWidth, 0,
                                    centerX + 2*sixthOfScreenWidth, screenWidth,
                                    centerY - 2*sixthOfScreenHeight, 0,
                                    centerY + 2*sixthOfScreenHeight, screenHeight);

        // calculate random positions within the layers:
        layer1.calculateRandomCirclePositionsInLayer(screenWidth, screenHeight);
        layer2.calculateRandomCirclePositionsInLayer(screenWidth, screenHeight);
        layer3.calculateRandomCirclePositionsInLayer(screenWidth, screenHeight);

        // sort the positions using anchor point:
        layer1.sortCircles();
        layer2.sortCircles();
        layer3.sortCircles();

        


    }


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