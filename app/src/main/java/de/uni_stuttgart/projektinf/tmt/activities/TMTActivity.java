package de.uni_stuttgart.projektinf.tmt.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import de.uni_stuttgart.projektinf.tmt.R;
import de.uni_stuttgart.projektinf.tmt.classes.Circle;

public class TMTActivity extends AppCompatActivity {
    int numberOfCircles = 8;
    int oldRandX = 0;
    int oldRandY = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TMTView(this));
        // initialize circle array:

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
        int randX = 0;
        int randY = 0;
        // zufällige Position (min 30px von alter Pos) bestimmen:
        boolean foundPos = false;
        while (!foundPos)
        {
            randX = (int)( Math.random() * (1024 - 2*Circle.RADIUS) ) + Circle.RADIUS;
            randY = (int)( Math.random() * (768 - 2*Circle.RADIUS) ) + Circle.RADIUS;

            //testen ob Entfernung zu voriger Position passt (euklidischer Abstand):
            int abstand = (int) Math.sqrt( (oldRandX - randX)*(oldRandX - randX) + (oldRandY - randY)*(oldRandY - randY) );
            if (abstand >= 30)
            {
                foundPos = true;
            }
        }
        oldRandX = randX;
        oldRandY = randY;

    }

    private void drawTMT(Canvas canvas){
        int x = canvas.getWidth();
        int y = canvas.getHeight();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4.5f);
        canvas.drawCircle(x / 2, y / 2, Circle.RADIUS, paint);
    }

}