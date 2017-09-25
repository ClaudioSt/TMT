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
import de.uni_stuttgart.projektinf.tmt.activities.TMTActivity;
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

    /**
     * Method init initializes the Paint needed for drawing onto the canvas
     */
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

    /**
     * Method onDraw is the overridden method of the AppCompatImageView.
     * Here the circles, their content and the path is drawn.
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // draw circles (and their content) on canvas:
        drawCircles(canvas);
        // draw the bitmap (containing the paths) to the canvas:
        canvas.drawBitmap(canvasBitmap, 0 , 0, canvasPaint);
        // draw the current path:
        canvas.drawPath(drawPath, drawPaint);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //create canvas of certain device size.
        super.onSizeChanged(w, h, oldw, oldh);

        //create Bitmap of certain width and height to store the paths:
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //apply bitmap to graphic to start drawing.
        drawCanvas = new Canvas(canvasBitmap);
    }

    /**
     * Method onTouchEvent is the overridden method of the AppCompatImageView.
     * This method is called when the user touches the display.
     * Depending on down, move or up action of the user, certain changes have to be made.
     *
     * @param event
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // get the point where the user touched the display:
        Point touchingPoint = new Point((int)event.getX(), (int) event.getY());
        // check this point for correct user action (user connected two correct circles):
        checkCircleTouch(touchingPoint);

        //respond to down, move and up events:
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                pathStartingPoint = touchingPoint;
                drawPath.moveTo(touchingPoint.x, touchingPoint.y);
                // set last wrong circle (which was colored/crossed red) back to normal:
                if (lastWrongCircle != null){
                    lastWrongCircle.setColor(Color.BLACK);
                    lastWrongCircle = null;
                }

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

    /**
     * Method checkCircleTouch is always called in onTouchEvent() to check if the user performed a
     * correct action by connecting two circles in the proper order.
     *
     * @param touchingPoint
     */
    private void checkCircleTouch(Point touchingPoint) {
        // first check if beginning started at last correct circle:
        if (pathStartsCorrect()){

            // go throw all circles to find the one that really got touched by the user:
            for(Circle circle : circleList) {
                // only look at circles that have not been touched already:
                if (!circle.gotTouched()) {
                    // check if user "really" touched circle:
                    if (circle.isInsideTheCircle(touchingPoint)) {
                        // if yes, look if it is the correct circle (respective order):
                        circle.checkIfCorrect(this);
                        break;
                    }
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

    public Circle getLastCorrectCircle(){
        if (TMTActivity.currentCircleNumber == 1)
            return null;
        else {
            // go through all circles and compare the sequencenumber to check if last correct:
            for (Circle circle : circleList) {
                if (circle.getSequenceNumberGlobal() == TMTActivity.currentCircleNumber - 1)
                    return circle;
            }
        }
        return null;
    }

    private boolean pathStartsCorrect(){
        Circle cl = getLastCorrectCircle();
        if (cl == null)
            return true;
        else
        {
            int distance = cl.getDistanceToPoint(pathStartingPoint);
            if (distance < Circle.RADIUS + Circle.TOLERANCE)
                return true;
            else
                return false;
        }
    }

    public void resetDrawPath(){
        drawPath.reset();
    }

    public void setPathStartingPoint(Point newStartingPoint){
        pathStartingPoint = newStartingPoint;
    }
}
