package de.uni_stuttgart.projektinf.tmt.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import de.uni_stuttgart.projektinf.tmt.R;

/**
 * The Choose Sequence Activity.
 * Enables the user to choose which sequence he/she wants for the TMT.
 *
 */
public class ChooseSequenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sequence);
    }

    /**
     * Method goToGoalChooser takes user to the next activity,
     * which is the choose goal activity.
     *
     * @param view
     * @see ChooseGoalActivity
     */
    public void goToGoalChooser(View view){
        Intent chooseGoalIntent = new Intent(this, ChooseGoalActivity.class);
        startActivity(chooseGoalIntent);
    }
}