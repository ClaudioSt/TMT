package de.uni_stuttgart.projektinf.tmt.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import de.uni_stuttgart.projektinf.tmt.R;


/**
 * The Choose Goal Activity.
 * Enables the user to choose which goal he/she wants for the TMT.
 *
 */
public class ChooseGoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_goal);
    }

    /**
     * Method goToTMT takes user to the next activity,
     * which is the actual tmt activity.
     *
     * @param view
     * @see TMTActivity
     */
    public void goToTMT(View view){
        Intent TMTIntent = new Intent(this, TMTActivity.class);
        startActivity(TMTIntent);
    }
}