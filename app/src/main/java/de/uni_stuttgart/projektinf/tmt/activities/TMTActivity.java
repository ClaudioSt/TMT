package de.uni_stuttgart.projektinf.tmt.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    private static final int NUMBEROFCIRCLES = 8;
    List<Circle> circleList = new ArrayList<Circle>();
    private TMTView tmtView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmt);
        tmtView = (TMTView)findViewById(R.id.tmt_view);

        // hide the action bar:
        getSupportActionBar().hide();
        // hide the status bar:
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // calculate positions of circles and send to View to draw:
        calculatePositions();
        tmtView.setCircles(circleList);
    }


    private void calculatePositions(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
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

                Log.i("myTag", "x: " + screenWidth);
                Log.i("myTag", "y: " + screenHeight);
                Log.i("myTag", "Circle Radius: " + Circle.RADIUS);
                Log.i("myTag", "Bla: " + (int)( Math.random() * (screenWidth - 2*Circle.RADIUS) ));

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
                    circleList.add(new Circle(randX, randY));
                }

            }
        }


    }



}