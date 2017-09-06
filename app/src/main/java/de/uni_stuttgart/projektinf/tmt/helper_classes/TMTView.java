package de.uni_stuttgart.projektinf.tmt.helper_classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import de.uni_stuttgart.projektinf.tmt.R;
import de.uni_stuttgart.projektinf.tmt.classes.Circle;

/**
 * The TMT View Class.
 * A custom view for drawing all the TMT content on canvas.
 */

public class TMTView extends AppCompatImageView{

    public static final boolean DELETEWRONGPATH = false;
    //drawing path
    private Path drawPath;
    //defines what to draw
    private Paint canvasPaint;
    //defines how to draw
    private Paint drawPaint;
    //initial color
    private int paintColor = Color.BLACK;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;
    //brush size
    private float currentBrushSize, lastBrushSize;
    // list of circles:
    List<Circle> circleList = new ArrayList<Circle>();
    // last wrong circle (for coloring / red-crossing):
    static Circle lastWrongCircle;
    // the starting point of the current path:
    private Point pathStartingPoint;
    // the end point of the current path:
    private Point pathEndPoint;

    public TMTView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        currentBrushSize = 10;
        lastBrushSize = currentBrushSize;

        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(currentBrushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCircles(canvas);
        canvas.drawBitmap(canvasBitmap, 0 , 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //create canvas of certain device size.
        super.onSizeChanged(w, h, oldw, oldh);

        //create Bitmap of certain w,h
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //apply bitmap to graphic to start drawing.
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point touchingPoint = new Point((int)event.getX(), (int) event.getY());
        checkCircleTouch(touchingPoint);

        //respond to down, move and up events:
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                pathStartingPoint = touchingPoint;
                drawPath.moveTo(touchingPoint.x, touchingPoint.y);
                // set last wrong circle (which was colored/crossed red) back to normal:
                if (lastWrongCircle != null)
                    lastWrongCircle.setColor(Color.BLACK);

                break;

            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchingPoint.x, touchingPoint.y);
                break;

            case MotionEvent.ACTION_UP:
                pathEndPoint = touchingPoint;
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;

            default:
                return false;
        }

        // redraw:
        invalidate();
        return true;
    }

    private void checkCircleTouch(Point touchingPoint) {
        // go throw all circles to check if touched:
        for(Circle circle : circleList) {
            // only look at circles that have not been touched already:
            if (!circle.gotTouched()) {
                // calculate distance from circle to touching point:
                int distance = circle.getDistanceToPoint(touchingPoint);
                // check if distance is ok and really touched:
                if (distance < Circle.RADIUS + Circle.TOLERANCE) {
                    // if yes, look if it is the correct circle (respective order):
                    circle.checkIfCorrect(drawPath);
                    break;
                }
            }
        }

    }

    private void drawCircles(Canvas canvas){

        for(Circle circle : circleList) {
            // setup paint to draw:
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            paint.setColor(circle.getColor());
            paint.setStrokeWidth(Circle.strokeWidth);

            // draw circle line:
            canvas.drawCircle(circle.getPosX(), circle.getPosY(), Circle.RADIUS, paint);

            // draw content:
            paint.setTextSize(Circle.contentTextSize);
            canvas.drawText(circle.getContent(), circle.getPosX(), circle.getPosY(), paint);
        }
    }

    public static void setWrongCircle(Circle cl){
        lastWrongCircle = cl;
    }

    public void setCircles(List<Circle> cl) {
        this.circleList = cl;
    }
}
