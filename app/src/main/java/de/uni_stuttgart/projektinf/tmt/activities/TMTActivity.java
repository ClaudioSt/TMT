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
import de.uni_stuttgart.projektinf.tmt.helper_classes.TMTView;

/**
 * The TMT Activity.
 * Here the user takes the actual Trail Making Test.
 *
 */
public class TMTActivity extends AppCompatActivity {

    public static final int NUMBEROFCIRCLES = 8;
    public static int currentCircleNumber = 1;
    List<Circle> circleList = new ArrayList<Circle>();
    private TMTView tmtView;
    private static Context thisContext;


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

        // calculate positions of circles and send to View to draw:
        calculateCirclePositions();
        tmtView.setCircles(circleList);
    }


    private void calculateCirclePositions(){
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


                //test if position is far away enough from others (euklidischer Abstand):
                boolean distanceIsOk = true;
                for(Circle otherCircle : circleList) {
                    int distance = (int) Math.sqrt( (otherCircle.getPosX() - randX)*(otherCircle.getPosX()- randX) + (otherCircle.getPosY() - randY)*(otherCircle.getPosY() - randY) );
                    if (distance < 4*Circle.RADIUS){
                        distanceIsOk = false;
                        break;
                    }
                }

                if (distanceIsOk){
                    foundPos = true;
                    Circle newCircle = new Circle(randX, randY, (i+1));
                    newCircle.setContent(""+(i+1));
                    circleList.add(newCircle);
                }

            }
        }


    }


    public static void TMTCompleted() {
        // takes user to the next activity:
        Intent viewResultsIntent = new Intent(thisContext, ViewResultsActivity.class);
        thisContext.startActivity(viewResultsIntent);
    }
}