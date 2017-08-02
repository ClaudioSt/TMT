package de.uni_stuttgart.projektinf.tmt.helper_classes;

/**
 * The TMT Calc class.
 * A class for calculating, processing and holding all the data and information necessary for the TMT.
 */

public class TMTCalc {

    // The time in seconds it takes to draw a line connecting all circles in the correct order:
    private int timeToCompletion;
    // The number of times a line is drawn to a circle in the incorrect order:
    private int numberOfErrors;
    // Total number of pauses that occurred during the test:
    private int numberOfPauses;
    // Total number of lifts that occurred during the test:
    private int numberOfLifts;
    // The average duration in seconds of all the pauses that occurred:
    private int averagePauseDuration;
    // The average duration in seconds of all the lifts of the stylus that occurred with regard to
    // the tablet screen:
    private int averageLiftDuration;
    // Average time in seconds spent drawing between circles:
    private int averageTimeBetweenCircles;
    // Drawing rate between circles:
    // (Rate is defined as the straight line distance between the exit point of one circle and the
    // entry point of the next, divided by the time spent drawing the line from one circle to
    // the next)
    private float averageRateBetweenCircles;
    // Average time in seconds spent drawing inside circles:
    private int averageTimeInsideCircles;
    // The average drawing rate inside each circle:
    private float averageRateInsideCircles;
    // The average drawing time in seconds before circles that contain letters (Part B only):
    private int averageTimeBeforeLetters;
    // The average drawing rate before circles that contain letters (Part B only):
    private float averageRateBeforeLetters;
    // The average drawing time in seconds before circles that contain numbers (Part B only):
    private int averageTimeBeforeNumbers;
    // The average drawing rate before circles that contain numbers (Part B only):
    private float averageRateBeforeNumbers;
    // Average pressure values are produced by the dTMT using Android hardware values. It is based
    // on how hard the user is pressing on the screen using the surface area of the pressed stylus:
    private int totalAveragePressure;
    // Average size values are produced by the dTMT using Android hardware values. It is also based
    // on how hard the user is pressing on the screen using the surface area of the pressed stylus:
    private int totalAverageSize;



}
