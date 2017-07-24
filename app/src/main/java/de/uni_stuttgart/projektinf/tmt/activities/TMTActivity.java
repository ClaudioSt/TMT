package de.uni_stuttgart.projektinf.tmt.activities;

import android.app.ActionBar;
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

public class TMTActivity extends AppCompatActivity {

    private static final int NUMBEROFCIRCLES = 8;
    List<Circle> circleList = new ArrayList<Circle>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TMTView(this));
        getSupportActionBar().hide();
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);


        calculatePositions();
    }

    public class TMTView extends View {
        public TMTView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawTMT(canvas);
        }

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

    private void drawTMT(Canvas canvas){

        for(Circle circle : circleList) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(4.5f);
            canvas.drawCircle(circle.getPosX() / 2, circle.getPosY() / 2, Circle.RADIUS, paint);
        }

    }

}